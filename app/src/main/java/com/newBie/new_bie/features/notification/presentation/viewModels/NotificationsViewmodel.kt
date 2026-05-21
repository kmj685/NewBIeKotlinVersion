package com.newBie.new_bie.features.notification.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.notification.domain.entities.NotificationsEntity
import com.newBie.new_bie.features.notification.domain.useCase.DeletedAllNotificationUseCase
import com.newBie.new_bie.features.notification.domain.useCase.DeletedNotificationUseCase
import com.newBie.new_bie.features.notification.domain.useCase.FetchNotificationsListUseCase
import com.newBie.new_bie.features.notification.domain.useCase.ReadAllNotificationUseCase
import com.newBie.new_bie.features.notification.domain.useCase.ReadNotificationUseCase
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // 힐트가 관리하는 뷰모델임을 선언
class NotificationViewModel @Inject constructor( // 생성자에 의존선을 주입하라고 명령
    private val fetchNotificationsListUseCase: FetchNotificationsListUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
    private val readAllNotificationUseCase: ReadAllNotificationUseCase,
    private val deletedNotificationUseCase: DeletedNotificationUseCase,
    private val deletedAllNotificationUseCase: DeletedAllNotificationUseCase
): ViewModel(){
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _notificationsList = MutableStateFlow<List<NotificationsEntity>>(emptyList())
    val notificationsList: StateFlow<List<NotificationsEntity>> = _notificationsList.asStateFlow()

    private val _isRead = MutableStateFlow(false)
    val isRead: StateFlow<Boolean> = _isRead

    // Realtime 구독 상태 추적 (중복 구독 방지)
    private var realtimeJob: Job? = null
    private var notificationChannel: RealtimeChannel? = null

    init {
        // 세션 상태를 감시해서 로그인 되면 자동으로 구독 시작
        observeAuthState()
    }


     // Auth 세션 상태를 감시해서 로그인이 완료되면 자동으로 Realtime 구독을 시작한다.
     // 로그아웃되면 구독을 해제한다.
    private fun observeAuthState() {
        viewModelScope.launch {
            SupabaseManager.supabase.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        Log.d(TAG, "로그인 감지 → Realtime 구독 시작")
                        getNotificationsList()
                        startRealtimeSubscription()
                    }
                    is SessionStatus.NotAuthenticated -> {
                        Log.d(TAG, "로그아웃 감지 → Realtime 구독 해제")
                        stopRealtimeSubscription()
                    }
                    else -> { /* Initializing, RefreshFailure 등은 무시 */ }
                }
            }
        }
    }

    // 알림 리스트
    fun getNotificationsList(){
        viewModelScope.launch {
            _isRefreshing.value = true

            // UseCase가 리턴한 Result 객체를 안전하게 풀어줍니다
            fetchNotificationsListUseCase()
                .onSuccess { list ->
                    _notificationsList.value = list
                }
                .onFailure { e ->
                    Log.e(TAG, "getNotificationsList 실패: ${e.message}")
                }

            _isRefreshing.value = false // 로딩 종료
        }
    }

    // 특정 알림 읽음 처리
    fun readNotification(id: Int){
        viewModelScope.launch {
            // 낙관적 업데이트
            val copyList = _notificationsList.value
            val copyIsRead = _isRead.value

            try {
                // UI부분 맵핑 작업
                val newList = _notificationsList.value.map {
                    if(it.id == id) {
                        it.copy(isRead = true)
                    } else it
                }
                _notificationsList.value = newList
                _isRead.value = if (newList.isEmpty()) true else newList.none { !it.isRead }
                
                // 서버 작업은 여기서 끝
                readNotificationUseCase(id)

            } catch (e: Exception){
                Log.e(TAG, "readNotification: ${e.message}", )
                // 서버 에러가 나서 뱉었다면 다시 백업
                _notificationsList.value = copyList
                _isRead.value = copyIsRead
            }
        }
    }

    // 알림 전체 읽음 처리
    fun readAllNotification(){
        viewModelScope.launch {
            // 낙관적 업데이트
            val copyList = _notificationsList.value
            val copyIsRead = _isRead.value
            val currentReceiverId = copyList.firstOrNull()?.receiverId ?: return@launch

            try {
                // UI부분 맵핑 작업
                _notificationsList.value = _notificationsList.value.map {
                        it.copy(isRead = true)
                }
                _isRead.value = true
                
                // 서버 작업은 여기서 끝
                readAllNotificationUseCase(currentReceiverId)
            } catch (e: Exception){
                Log.e(TAG, "readAllNotification: ${e.message}")
                _notificationsList.value = copyList
                _isRead.value = copyIsRead
            }
        }
    }

    // 특정 알림 삭제 처리
    fun deletedNotification(id: Int){
        viewModelScope.launch {
            // 낙관적 업데이트
            val copyList = _notificationsList.value
            val copyIsRead = _isRead.value

            try {
                // UI 부분
                val newList = _notificationsList.value.filter {
                    it.id != id // 이것의 id가 삭제할 번호와 같은 것은 다 빼고 다시 재정렬
                }
                _notificationsList.value = newList
                _isRead.value = if (newList.isEmpty()) true else newList.none { !it.isRead }
                
                // 서버 작업은 여기서 끝
                deletedNotificationUseCase(id)

            } catch (e: Exception){
                Log.e(TAG, "deletedNotification: ${e.message}", )
                _notificationsList.value = copyList
                _isRead.value = copyIsRead
            }
        }
    }

    // 알림 전체 삭제 처리
    fun deletedAllNotification(){
        viewModelScope.launch {
            // 낙관적 업데이트
            val copyList = _notificationsList.value
            val copyIsRead = _isRead.value
            val currentReceiverId = copyList.firstOrNull()?.receiverId ?: return@launch

            try {
                // UI 부분
                _notificationsList.value = _notificationsList.value.filter {
                    it.receiverId != currentReceiverId // 이것의 id가 삭제할 번호와 같은 것은 다 빼고 다시 재정렬
                }
                _isRead.value = true
                
                // 서버 작업은 여기서 끝
                deletedAllNotificationUseCase(currentReceiverId)
            } catch (e: Exception){
                Log.e(TAG, "deletedNotification: ${e.message}", )
                _notificationsList.value = copyList
                _isRead.value = copyIsRead
            }
        }
    }


     // Realtime 구독 시작 (로그인 후 호출됨)
     // 중복 구독을 방지하기 위해 기존 구독이 있으면 먼저 해제한다
    private fun startRealtimeSubscription() {
        // 이미 구독 중이면 중복 방지
        if (realtimeJob?.isActive == true) {
            Log.d(TAG, "Realtime 이미 구독 중 → 스킵")
            return
        }

        realtimeJob = viewModelScope.launch {
            val myId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
            if (myId == null) {
                Log.e(TAG, "Realtime 구독 실패: 유저 ID가 null")
                return@launch
            }
            Log.d(TAG, "Realtime 구독 시작 - myId: $myId")

            // 초기 상태 세팅 (앱을 방금 켰으니 현재 안읽은 알림이 있는지 1회성 확인)
            checkUnreadCount(myId)

            try {
                // Realtime 채널 생성 (supabase-kt v3 extension function 사용)
                val channel = SupabaseManager.supabase.channel("notification_channel")
                notificationChannel = channel

                // 데이터 흐름(Flow)을 생성하고 즉시 collect(구독 시작)
                val changes = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "notifications"
                    // 나에게 온 알림만 필터링해서 받는다.
                    filter("receiver_id", FilterOperator.EQ, myId)
                }
                changes
                    .onEach { action ->
                        Log.d(TAG, "Realtime 이벤트 수신: ${action::class.simpleName}")
                        when (action) {
                            is PostgresAction.Insert -> {
                                // 새 알림 발생 -> 빨간 점 켜짐
                                Log.d(TAG, "새 알림 Insert 감지!")
                                _isRead.value = false
                                getNotificationsList() // 리스트도 새로고침해서 UI에서 바로 보여주기
                            }
                            is PostgresAction.Update, is PostgresAction.Delete -> {
                                // 알림 상태가 수정되거나 삭제 됐을 때 상태가 변했으니 다시 개수 세기
                                Log.d(TAG, "알림 Update/Delete 감지!")
                                checkUnreadCount(myId)
                                getNotificationsList() // 리스트도 새로고침해서 UI에서 바로 보여주기
                            }
                            is PostgresAction.Select -> return@onEach
                        }
                    }.launchIn(this) // this = 현재 열려있는 코루틴 스코프

                // 채널 구독 (내부적으로 웹소켓 연결도 처리됨)
                channel.subscribe()
                Log.d(TAG, "Realtime 채널 구독 완료! 상태: ${channel.status.value}")

            } catch (e: Exception) {
                Log.e(TAG, "Realtime 구독 중 에러: ${e.message}", e)
            }
        }
    }


     // Realtime 구독 해제 (로그아웃 시 호출됨)
    private fun stopRealtimeSubscription() {
        realtimeJob?.cancel()
        realtimeJob = null
        viewModelScope.launch {
            try {
                notificationChannel?.unsubscribe()
                notificationChannel = null
                Log.d(TAG, "Realtime 채널 구독 해제 완료")
            } catch (e: Exception) {
                Log.e(TAG, "Realtime 해제 에러: ${e.message}")
            }
        }
    }

    // 안읽은 알림이 1개라도 있는지 확인
    private suspend fun checkUnreadCount(myId: String){
        try {
            val count = SupabaseManager.supabase.postgrest["notifications"]
                .select {
                    head = true
                    count(io.github.jan.supabase.postgrest.query.Count.EXACT)
                    filter {
                        eq("receiver_id", myId)
                        eq("is_read", false)
                    }
                }.countOrNull()

            if (count != null) {
                if (count == 0L) {
                    _isRead.value = true   // 안 읽은 게 0개니까 다 읽음
                } else {
                    _isRead.value = false  // 안 읽은 게 1개 이상이니까 안 읽음 -> 빨간점 표시
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "checkUnreadCount: ${e.message}", )
        }
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel 소멸 시 채널 정리
        viewModelScope.launch {
            try {
                notificationChannel?.unsubscribe()
                Log.d(TAG, "ViewModel onCleared → 채널 정리 완료")
            } catch (_: Exception) { }
        }
    }
}
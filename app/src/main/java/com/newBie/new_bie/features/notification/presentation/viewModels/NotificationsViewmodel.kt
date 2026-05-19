package com.newBie.new_bie.features.notification.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.notification.domain.entities.NotificationsEntity
import com.newBie.new_bie.features.notification.domain.useCase.DeletedAllNotificationUseCase
import com.newBie.new_bie.features.notification.domain.useCase.DeletedNotificationUseCase
import com.newBie.new_bie.features.notification.domain.useCase.FetchNotificationsListUseCase
import com.newBie.new_bie.features.notification.domain.useCase.ReadAllNotificationUseCase
import com.newBie.new_bie.features.notification.domain.useCase.ReadNotificationUseCase
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        getNotificationsList()
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

            try {
                // UI부분 맵핑 작업
                _notificationsList.value = _notificationsList.value.map {
                    if(it.id == id) {
                        it.copy(isRead = true)
                    } else it
                }
                // 서버 작업은 여기서 끝
                readNotificationUseCase(id)

            } catch (e: Exception){
                Log.e(TAG, "readNotification: ${e.message}", )
                // 서버 에러가 나서 뱉었다면 다시 백업
                _notificationsList.value = copyList
            }
        }
    }

    // 알림 전체 읽음 처리
    fun readAllNotification(){
        viewModelScope.launch {
            // 낙관적 업데이트
            val copyList = _notificationsList.value
            val currentReceiverId = copyList.firstOrNull()?.receiverId ?: return@launch

            try {
                // UI부분 맵핑 작업
                _notificationsList.value = _notificationsList.value.map {
                        it.copy(isRead = true)
                }
                // 서버 작업은 여기서 끝
                readAllNotificationUseCase(currentReceiverId)
            } catch (e: Exception){
                Log.e(TAG, "readAllNotification: ${e.message}")
                _notificationsList.value = copyList
            }
        }
    }

    // 특정 알림 삭제 처리
    fun deletedNotification(id: Int){
        viewModelScope.launch {
            // 낙관적 업데이트
            val copyList = _notificationsList.value

            try {
                // UI 부분
                _notificationsList.value = _notificationsList.value.filter {
                    it.id != id // 이것의 id가 삭제할 번호와 같은 것은 다 빼고 다시 재정렬
                }
                // 서버 작업은 여기서 끝
                deletedNotificationUseCase(id)

            } catch (e: Exception){
                Log.e(TAG, "deletedNotification: ${e.message}", )
                _notificationsList.value = copyList
            }
        }
    }

    // 알림 전체 삭제 처리
    fun deletedAllNotification(){
        viewModelScope.launch {
            // 낙관적 업데이트
            val copyList = _notificationsList.value
            val currentReceiverId = copyList.firstOrNull()?.receiverId ?: return@launch

            try {
                // UI 부분
                _notificationsList.value = _notificationsList.value.filter {
                    it.receiverId != currentReceiverId // 이것의 id가 삭제할 번호와 같은 것은 다 빼고 다시 재정렬
                }
                // 서버 작업은 여기서 끝
                deletedAllNotificationUseCase(currentReceiverId)
            } catch (e: Exception){
                Log.e(TAG, "deletedNotification: ${e.message}", )
                _notificationsList.value = copyList

            }
        }
    }
}
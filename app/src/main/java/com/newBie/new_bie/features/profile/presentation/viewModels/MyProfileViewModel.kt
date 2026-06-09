package com.newBie.new_bie.features.profile.presentation.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import com.newBie.new_bie.features.profile.data.repositories.ProfileRepositoryImpl
import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import com.newBie.new_bie.features.profile.domain.entities.GuestbooksEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import com.newBie.new_bie.features.profile.domain.usecase.followUseCase.CheckFollowStatusUseCase
import com.newBie.new_bie.features.profile.domain.usecase.followUseCase.FetchUserPostsUseCase
import com.newBie.new_bie.features.profile.domain.usecase.followUseCase.FetchUserUseCase
import com.newBie.new_bie.features.profile.domain.usecase.followUseCase.FollowUserUseCase
import com.newBie.new_bie.features.profile.domain.usecase.followUseCase.GetFollowerListUseCase
import com.newBie.new_bie.features.profile.domain.usecase.followUseCase.GetFollowingListUseCase
import com.newBie.new_bie.features.profile.domain.usecase.followUseCase.GetMyFollowingIdsUseCase
import com.newBie.new_bie.features.profile.domain.usecase.followUseCase.GetUserUseCase
import com.newBie.new_bie.features.profile.domain.usecase.followUseCase.UnfollowUserUseCase
import com.newBie.new_bie.features.profile.domain.usecase.guestbooksUseCase.GetGuestbooksListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, //targetId를 Hilt가 알아서 넣어준다.
    private val checkFollowStatusUseCase: CheckFollowStatusUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchUserPostsUseCase: FetchUserPostsUseCase,
    private val getGuestbooksListUseCase: GetGuestbooksListUseCase
    ) : ViewModel() {

    // 유저 정보
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    // 다른 유저
    // 로그인한 내 ID와 타인의 ID가 유동적으로 바뀌거나, "값이 없을 수도 있고 나중에 코드로 언제든 변경할 가능성이 있는 상태(State)"를 다룰 때
    private val _targetUserId = MutableStateFlow<String?>(savedStateHandle.get<String>("userId"))
    // 게시글 목록
    private val _posts = MutableStateFlow<List<PostWithProfileEntity>>(emptyList())
    val posts: StateFlow<List<PostWithProfileEntity>> = _posts

    // 페이지네이션
    private val perPageCount = 12
    // 포스트 전용 페이지 관리 변수
    private var postCurrentOffset = 0

    // 다음페이지 무한 호출 방지
    private var isPostPageLoading = false
    private var isPostLastPage = false
    private var postFetchJob: Job? = null // 현재 실행 중인 새로고침 작업을 저장
    private var postPageJob: Job? = null // 페이징 작업을 저장

    // 방명록 전용 페이징 관리 변수
    private var guestbookCurrentOffset = 0
    private var isGuestbookPageLoading = false
    private var isGuestbookLastPage = false
    private var guestbookFetchJob: Job? = null
    private var guestbookPageJob: Job? = null

    // 버튼 노출 여부 (스크롤 위치용)
    private val _buttonIsWorking = MutableStateFlow(false)
    val buttonIsWorking: StateFlow<Boolean> = _buttonIsWorking

    // refresh 여부
    var isRefreshing = MutableStateFlow(false)

    // 팔로잉 여부
    private val _isFollowing = MutableStateFlow(false)
    val isFollowing: StateFlow<Boolean> = _isFollowing

    // 마이프로필이니까 팔로우 버튼 토글하려면 팔로워 리스트만 필요
    private val _followerList = MutableStateFlow<List<FollowEntity>>(emptyList())

    private val _guestbooks = MutableStateFlow<List<GuestbooksEntity>>(emptyList())
    val guestbooks = _guestbooks.asStateFlow()




    init {
        fetchUser()
        fetchPosts()
        fetchGuestbooks()
    }

    fun fetchTargetUser(userId: String?){
        _targetUserId.value = userId
        refreshAll()
    }

    fun fetchUser() {
        viewModelScope.launch {
            val userId = _targetUserId.value ?: SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?: return@launch

            val result = fetchUserUseCase(userId)

            result.onSuccess {
                _user.value = it
            }.onFailure {
                _user.value = null
            }
            // 팔로우 상태도 확인
            checkFollowStatUs(userId)
        }
    }

    fun fetchPosts() {
        postFetchJob?.cancel() // 새로운 요청이 오면 기존 작업 취소
        postPageJob?.cancel()  // 새로고침 시 이전에 진행중던 페이징 작업도 취소

        postFetchJob = viewModelScope.launch {
            val userId = _targetUserId.value ?: SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?: return@launch

            try {
                isRefreshing.value = true
                isPostPageLoading = false // 페이지 로딩 강제 초기화
                postCurrentOffset = 0
                isPostLastPage = false // 새로고침 시 마지막 페이지 여부 초기화
                val result = fetchUserPostsUseCase(
                    targetUserId = userId,
                    currentIndex = 0,
                    perPage = perPageCount
                )
                result.onSuccess {
                    if (it.isNotEmpty()) {
                        _posts.value = it
                        postCurrentOffset = it.size // 누적하지 않고 그대로 업데이트
                        // 가져온 갯수가 perPageCount보다 작으면 남은 게시글이 없으므로 마지막 페이지
                        isPostLastPage = it.size < perPageCount
                    } else {
                        _posts.value = emptyList()
                        postCurrentOffset = 0
                        isPostLastPage = true
                    }
                }
            } catch (e: Exception){
                if (e !is CancellationException) {
                    Log.d(Constants.TAG, "fetchPosts 에러: $e")
                } else throw e
            } finally {
                // 캔슬되어 이전 Job이 종료될 때 최신 트래킹 상태를 덮어씌우는 것을 방지
                if (postFetchJob == coroutineContext[Job]) {
                    isRefreshing.value = false
                }
            }
        }
    }

    fun fetchMorePosts() {
        // 이미 로딩 중이거나, 마지막 페이지이면 중복 요청 방지
        if (isPostPageLoading || isPostLastPage) return
        // 새로고침 작업이 진행중이면 충돌을 막기위해 진입 차단
        if (isRefreshing.value || postFetchJob?.isActive == true) return

        // [수정] 다른 사람 프로필일 때도 안전하게 그 사람의 ID를 가져오도록 조치
        val userId = _targetUserId.value ?: SupabaseManager.supabase.auth.currentUserOrNull()?.id
        ?: return

        isPostPageLoading = true // 코루틴 밖에서 선언하여 여러번 진입 방지

        postPageJob = viewModelScope.launch {
            try {
                val result = fetchUserPostsUseCase(
                    targetUserId = userId,
                    currentIndex = postCurrentOffset,
                    perPage = perPageCount
                )

                result.onSuccess { morePosts ->
                    if (morePosts.isNotEmpty()){
                        _posts.update { old ->
                            val merged = (old + morePosts).distinctBy { it.id }
                            postCurrentOffset = merged.size
                            merged
                        }
                        isPostLastPage = morePosts.size < perPageCount
                    } else {
                        isPostLastPage = true // 더 이상 가져올 항목이 없음
                    }
                } .onFailure {e ->
                    Log.e(Constants.TAG, "fetchMorePosts: ${e.message}", )
                }
            } catch (e: Exception){
                if (e !is CancellationException) {
                    Log.e(Constants.TAG, "fetchMorePosts 에러: $e")
                } else throw e
            } finally {
                // 이전 페이징 Job이 취소될 떄 최신 트래킹 상태를 덮어씌우는 것을 방지
                if (postPageJob == coroutineContext[Job]) {
                    isPostPageLoading = false // 로딩 완료
                }
            }
        }
    }

    fun refreshAll() {
        isRefreshing.value = true // 새로고침 애니메이션 트리거
        fetchUser()
        fetchPosts()
        fetchGuestbooks()
    }

    fun unRegister() {
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?: return@launch

//            SupabaseManager.shared.unRegister(userId)
//            SupabaseManager.shared.supabase.auth.signOut()
        }
    }

    fun toggleFollow(targetUserId: String) {
        viewModelScope.launch {
            val myId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: return@launch

            // 1. 현재 보고 있는 유저의 팔로우 상태 (상단 버튼용)
            val prevIsFollowing = _isFollowing.value

            // 2. 팔로우 리스트 데이터 (수정 가능한 데이터로 복사 과정)
            val followerListCopy = _followerList.value.toMutableList() // 수정 가능하게
            val followerIndex = followerListCopy.indexOfFirst { it.users.id == targetUserId } // 전체 리스트가 아닌 리스트 중 하나만 콕 집어서 바꿔야하기 때문에 index를 받는다.

            // [낙관적 업데이트]
            // 상단 버튼 상태 즉시 변경
            _isFollowing.value = !prevIsFollowing // 일단 UI만 바로 변경(현재 상태가 팔로잉이던 팔로우던 반대로 변경)

            // 만약 리스트에 해당 유저가 존재한다면, 해당 항목의 팔로우 상태도 즉시 변경하여 리스트 UI를 갱신
            if (followerIndex != -1) { // -1은 "찾으려고 하는 대상이 목록에 없다"는 것을 의미하는 약속된 신호
                followerListCopy[followerIndex] = followerListCopy[followerIndex].copy(isFollowing = !prevIsFollowing)
                _followerList.value = followerListCopy
            }

            // [서버 통신]
            try {
                if (!prevIsFollowing) { // _isFollowing.value = false라면
                    followUserUseCase(myId = myId, targetUserId = targetUserId).getOrThrow()
                } else { // 나머지 _isFollowing.value = true라면
                    unfollowUserUseCase(myId = myId, targetUserId = targetUserId).getOrThrow()
                }

                // 성공 시: 최신 유저 정보(팔로워 숫자 등)를 다시 가져와서 UI 갱신
                fetchUser()

            } catch (e: Exception) { // 서버 통신부분 없이 UI 프론트 단에서
                // [롤백] 실패 시 다시 원복
                _isFollowing.value = prevIsFollowing
                if (followerIndex != -1) {
                    followerListCopy[followerIndex] = followerListCopy[followerIndex].copy(isFollowing = prevIsFollowing)
                    _followerList.value = followerListCopy
                }
                Log.e(Constants.TAG, "toggleFollow 실패: $e")
            }
        }
    }

    fun checkFollowStatUs(targetUserId: String){
        Log.d(Constants.TAG, "팔로우 상태 체크 시작: $targetUserId")
        viewModelScope.launch {
            val myId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: return@launch
            if (myId == targetUserId) return@launch

            val result = checkFollowStatusUseCase(myId = myId, targetUserId = targetUserId)

            result.onSuccess {
                Log.d(Constants.TAG, "서버 응답 결과: $it")
                _isFollowing.value = it
            }.onFailure {
                _isFollowing.value = false
            }
        }
    }

    fun fetchGuestbooks(){
        guestbookFetchJob?.cancel() // 새로운 요청이 오면 기존 작업 취소
        guestbookPageJob?.cancel()  // 새로고침 시 이전에 진행중던 페이징 작업도 취소

        guestbookFetchJob = viewModelScope.launch {
            val userId = _targetUserId.value ?: SupabaseManager.supabase.auth.currentUserOrNull()?.id
            ?: return@launch

            try {
                isRefreshing.value = true
                isGuestbookPageLoading = false // 페이지 로딩 강제 초기화
                guestbookCurrentOffset = 0
                isGuestbookLastPage = false // 새로고침 시 마지막 페이지 여부 초기화
                val result = getGuestbooksListUseCase(
                    receiverId = userId,
                    currentIndex = 0,
                    perPage = perPageCount
                )
                result.onSuccess {
                    if (it.isNotEmpty()) {
                        _guestbooks.value = it
                        guestbookCurrentOffset = it.size // 누적하지 않고 그대로 업데이트
                        // 가져온 갯수가 perPageCount보다 작으면 남은 게시글이 없으므로 마지막 페이지
                        isGuestbookLastPage = it.size < perPageCount
                    } else {
                        _guestbooks.value = emptyList()
                        guestbookCurrentOffset = 0
                        isGuestbookLastPage = true
                    }
                }
            } catch (e: Exception){
                if (e !is CancellationException) {
                    Log.d(Constants.TAG, "fetchGuestbooks 에러: ${e.message}")
                } else throw e
            } finally {
                // 캔슬되어 이전 Job이 종료될 때 최신 트래킹 상태를 덮어씌우는 것을 방지
                if (guestbookFetchJob == coroutineContext[Job]) {
                    isRefreshing.value = false
                }
            }
        }
    }

    fun fetchMoreGuestbooks() {
        // 이미 로딩 중이거나, 마지막 페이지이면 중복 요청 방지
        if (isGuestbookPageLoading || isGuestbookLastPage) return
        // 새로고침 작업이 진행중이면 충돌을 막기위해 진입 차단
        if (isRefreshing.value || guestbookFetchJob?.isActive == true) return

        // [수정] 다른 사람 프로필일 때도 안전하게 그 사람의 ID를 가져오도록 조치
        val userId = _targetUserId.value ?: SupabaseManager.supabase.auth.currentUserOrNull()?.id
        ?: return

        isGuestbookPageLoading = true // 코루틴 밖에서 선언하여 여러번 진입 방지

        guestbookPageJob = viewModelScope.launch {
            try {
                val result = getGuestbooksListUseCase(
                    receiverId = userId,
                    currentIndex = guestbookCurrentOffset,
                    perPage = perPageCount
                )

                result.onSuccess { moreGuestbooks ->
                    if (moreGuestbooks.isNotEmpty()){
                        _guestbooks.update { old ->
                            val merged = (old + moreGuestbooks).distinctBy { it.id }
                            guestbookCurrentOffset = merged.size
                            merged
                        }
                        isGuestbookLastPage = moreGuestbooks.size < perPageCount
                    } else {
                        isGuestbookLastPage = true // 더 이상 가져올 항목이 없음
                    }
                } .onFailure {e ->
                    Log.e(Constants.TAG, "fetchMoreGuestbooks: ${e.message}", )
                }
            } catch (e: Exception){
                if (e !is CancellationException) {
                    Log.e(Constants.TAG, "fetchMoreGuestbooks 에러: $e")
                } else throw e
            } finally {
                // 이전 페이징 Job이 취소될 떄 최신 트래킹 상태를 덮어씌우는 것을 방지
                if (guestbookPageJob == coroutineContext[Job]) {
                    isGuestbookPageLoading = false // 로딩 완료
                }
            }
        }
    }
}

//class MyProfileViewModelFactory(
//    private val targetUserId: String?
//) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST") //컴파일러 경고를 끄는 어노테이션
//    // create 뷰모델을 생성할 때
//    // <T : ViewModel> 어떤 뷰모델이던 제네릭으로 다 찍어낼 수 있다.
//    // modelClass: Class<T> 지금 안드로이드가 나한테 무슨 뷰모델을 만들어달라고 요청한건지 정보가 이 매개변수로 들어옴
//    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
//        // modelClass.isAssignableFrom: MyProfileViewModel이 뷰모델이 맞는지 확인
//        if (modelClass.isAssignableFrom(MyProfileViewModel::class.java)) {
//            // MyProfileViewModel이 뷰모델이 맞다면 targetUserId를 넣어서 뷰모델을 수동으로 생성
//            return MyProfileViewModel(targetUserId) as T
//        }
//        // MyProfileViewModel이 뷰모델이 아니라면 오류났다고 던져라
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
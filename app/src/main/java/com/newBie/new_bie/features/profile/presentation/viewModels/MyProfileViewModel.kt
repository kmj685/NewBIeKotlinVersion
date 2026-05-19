package com.newBie.new_bie.features.profile.presentation.viewModels

import android.util.Log
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.bumptech.glide.integration.compose.placeholder
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import com.newBie.new_bie.features.profile.data.repositories.ProfileRepositoryImpl
import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import com.newBie.new_bie.features.profile.domain.usecase.CheckFollowStatusUseCase
import com.newBie.new_bie.features.profile.domain.usecase.FollowUserUseCase
import com.newBie.new_bie.features.profile.domain.usecase.UnfollowUserUseCase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyProfileViewModel(private val targetUserId: String?) : ViewModel() {
        private val repository : PostRepository = PostRepositoryImpl()
        private val profileRepository : ProfileRepository = ProfileRepositoryImpl()

    // 유저 정보
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    // 다른 유저
    private val _targetUserId = MutableStateFlow<String?>(targetUserId)

    // 게시글 목록
    private val _posts = MutableStateFlow<List<PostWithProfileEntity>>(emptyList())
    val posts: StateFlow<List<PostWithProfileEntity>> = _posts

    // 페이지네이션
    private var currentOffset = 0
    private val perPageCount = 12

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

    // 다음페이지 무한 호출 방지
    private var isPageLoading = false
    private var isLastPage = false
    private var fetchJob: Job? = null // 현재 실행 중인 새로고침 작업을 저장
    private var pageJob: Job? = null // 페이징 작업을 저장

    // UseCase
    private val followUserUseCase = FollowUserUseCase(profileRepository)
    private val unfollowUserUseCase = UnfollowUserUseCase(profileRepository)
    private val checkFollowStatusUseCase = CheckFollowStatusUseCase(profileRepository)



    init {
        fetchUser()
        fetchPosts()
    }

    fun fetchTargetUser(userId: String?){
        _targetUserId.value = userId
        refreshAll()
    }

    fun fetchUser() {
        viewModelScope.launch {
            val userId = _targetUserId.value ?: SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?: return@launch

            _user.value = repository.fetchUser(userId)

            // 팔로우 상태도 확인
            checkFollowStatUs(userId)
        }
    }

    fun fetchPosts() {
        fetchJob?.cancel() // 새로운 요청이 오면 기존 작업 취소
        pageJob?.cancel()  // 새로고침 시 이전에 진행중던 페이징 작업도 취소

        fetchJob = viewModelScope.launch {
            val userId = _targetUserId.value ?: SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?: return@launch

            try {
                isRefreshing.value = true
                isPageLoading = false // 페이지 로딩 강제 초기화
                currentOffset = 0
                isLastPage = false // 새로고침 시 마지막 페이지 여부 초기화
                val result = repository.fetchUserPosts(
                    userId = userId,
                    currentIndex = 0,
                    perPage = perPageCount
                )
                if (result.isNotEmpty()) {
                    _posts.value = result
                    currentOffset = result.size // 누적하지 않고 그대로 업데이트
                    // 가져온 갯수가 perPageCount보다 작으면 남은 게시글이 없으므로 마지막 페이지
                    isLastPage = result.size < perPageCount 
                } else {
                    _posts.value = emptyList()
                    currentOffset = 0
                    isLastPage = true
                }
            } catch (e: Exception){
                if (e !is CancellationException) {
                    Log.d(Constants.TAG, "fetchPosts 에러: $e")
                } else throw e
            } finally {
                // 캔슬되어 이전 Job이 종료될 때 최신 트래킹 상태를 덮어씌우는 것을 방지
                if (fetchJob == coroutineContext[Job]) {
                    isRefreshing.value = false
                }
            }
        }
    }

    fun fetchMorePosts() {
        // 이미 로딩 중이거나, 마지막 페이지이면 중복 요청 방지
        if (isPageLoading || isLastPage) return
        // 새로고침 작업이 진행중이면 충돌을 막기위해 진입 차단
        if (isRefreshing.value || fetchJob?.isActive == true) return

        val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
            ?: return

        isPageLoading = true // 코루틴 밖에서 선언하여 여러번 진입 방지

        pageJob = viewModelScope.launch {
            try {
                val morePosts = repository.fetchUserPosts(
                    userId = userId,
                    currentIndex = currentOffset,
                    perPage = perPageCount
                )
                if (morePosts.isNotEmpty()){
                    _posts.update { old ->
                        val merged = (old + morePosts).distinctBy { it.id }
                        currentOffset = merged.size
                        merged
                    }
                    isLastPage = morePosts.size < perPageCount
                } else {
                    isLastPage = true // 더 이상 가져올 항목이 없음
                }
            } catch (e: Exception){
                if (e !is CancellationException) {
                    Log.e(Constants.TAG, "fetchMorePosts 에러: $e")
                } else throw e
            } finally {
                // 이전 페이징 Job이 취소될 떄 최신 트래킹 상태를 덮어씌우는 것을 방지
                if (pageJob == coroutineContext[Job]) {
                    isPageLoading = false // 로딩 완료
                }
            }
        }
    }

    fun refreshPosts() {
        fetchPosts()
    }

    fun refreshAll() {
        isRefreshing.value = true // 새로고침 애니메이션 트리거
        fetchUser()
        fetchPosts()
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
}

class MyProfileViewModelFactory(
    private val targetUserId: String?
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST") //컴파일러 경고를 끄는 어노테이션
    // create 뷰모델을 생성할 때
    // <T : ViewModel> 어떤 뷰모델이던 제네릭으로 다 찍어낼 수 있다.
    // modelClass: Class<T> 지금 안드로이드가 나한테 무슨 뷰모델을 만들어달라고 요청한건지 정보가 이 매개변수로 들어옴
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        // modelClass.isAssignableFrom: MyProfileViewModel이 뷰모델이 맞는지 확인
        if (modelClass.isAssignableFrom(MyProfileViewModel::class.java)) {
            // MyProfileViewModel이 뷰모델이 맞다면 targetUserId를 넣어서 뷰모델을 수동으로 생성
            return MyProfileViewModel(targetUserId) as T
        }
        // MyProfileViewModel이 뷰모델이 아니라면 오류났다고 던져라
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
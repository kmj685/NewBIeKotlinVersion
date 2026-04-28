package com.newBie.new_bie.features.profile.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.integration.compose.placeholder
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyProfileViewModel : ViewModel() {
        private val repository : PostRepository = PostRepositoryImpl()

    // 유저 정보
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

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

    // 다음페이지 무한 호출 방지
    private var isPageLoading = false
    private var isLastPage = false
    private var fetchJob: Job? = null // 현재 실행 중인 새로고침 작업을 저장
    private var pageJob: Job? = null // 페이징 작업을 저장

    init {
        fetchUser()
        fetchPosts()
    }

    fun fetchUser() {
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?: return@launch

            _user.value = repository.fetchUser(userId)
        }
    }

    fun fetchPosts() {
        fetchJob?.cancel() // 새로운 요청이 오면 기존 작업 취소
        pageJob?.cancel()  // 새로고침 시 이전에 진행중던 페이징 작업도 취소

        fetchJob = viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
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
                if (e !is kotlinx.coroutines.CancellationException) {
                    Log.d(Constants.TAG, "fetchPosts 에러: $e")
                } else throw e
            } finally {
                // 캔슬되어 이전 Job이 종료될 때 최신 트래킹 상태를 덮어씌우는 것을 방지
                if (fetchJob == coroutineContext[kotlinx.coroutines.Job]) {
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
                if (e !is kotlinx.coroutines.CancellationException) {
                    Log.e(Constants.TAG, "fetchMorePosts 에러: $e")
                } else throw e
            } finally {
                // 이전 페이징 Job이 취소될 떄 최신 트래킹 상태를 덮어씌우는 것을 방지
                if (pageJob == coroutineContext[kotlinx.coroutines.Job]) {
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
}
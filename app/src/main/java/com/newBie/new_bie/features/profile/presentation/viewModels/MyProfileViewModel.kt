package com.newBie.new_bie.features.profile.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyProfileViewModel : ViewModel() {

    // 유저 정보
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    // 게시글 목록
    private val _posts = MutableStateFlow<List<PostWithProfileEntity>>(emptyList())
    val posts: StateFlow<List<PostWithProfileEntity>> = _posts

    // 페이지네이션
    private var currentPage = 1

    // 버튼 노출 여부 (스크롤 위치용)
    private val _buttonIsWorking = MutableStateFlow(false)
    val buttonIsWorking: StateFlow<Boolean> = _buttonIsWorking

    init {
        fetchUser()
        fetchPosts()
    }

    fun fetchUser() {
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?: return@launch

//            _user.value = SupabaseManager.supabase.fetchUser(userId)
        }
    }

    fun fetchPosts() {
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?: return@launch

            currentPage = 1
//            _posts.value = NetworkApiManager.shared.fetchUserPosts(
//                userId = userId,
//                currentIndex = currentPage
//            )
        }
    }

    fun fetchMorePosts() {
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?: return@launch

            currentPage++

//            val morePosts = NetworkApiManager.shared.fetchUserPosts(
//                userId = userId,
//                currentIndex = currentPage
//            )

//            _posts.value = _posts.value + morePosts
        }
    }

    fun refreshPosts() {
        fetchPosts()
    }

    fun refreshAll() {
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

    /**
     * 스크롤 위치에 따른 버튼 상태 처리
     * (Compose or RecyclerView Scroll 콜백에서 호출)
     */
    fun onScroll(offset: Int) {
        _buttonIsWorking.value = offset >= 50
    }
}
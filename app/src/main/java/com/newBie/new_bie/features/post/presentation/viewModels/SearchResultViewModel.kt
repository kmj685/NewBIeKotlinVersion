package com.newBie.new_bie.features.post.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchResultViewModel : ViewModel() {

    val repository: PostRepository = PostRepositoryImpl()
    val posts = MutableStateFlow<List<PostWithProfileEntity>>(listOf())
    val users = MutableStateFlow<List<UserEntity>>(listOf())
    val keyword = MutableStateFlow<String>("")

    val selectedTab = MutableStateFlow<Int>(0)

    var comments : MutableStateFlow<List<CommentWithProfileEntity>> = MutableStateFlow<List<CommentWithProfileEntity>>(listOf())
    var selectPostId : MutableStateFlow<Int?> = MutableStateFlow(null)

    var userCommentInput: MutableStateFlow<String> = MutableStateFlow("")

    private var userCurrentPage = 1
    private var postCurrentPage = 1

    private var isUserLoading = false
    private var isPostLoading = false

    // --- Actions ---

    fun onTabSelected(index: Int) {
        selectedTab.value = index
    }

    // 홈화면 또는 결과페이지에서 검색 실행
    fun search(searchKeyword: String) {
        if (searchKeyword.isBlank()) return // 빈 값 방어 코드 추가 추천

        viewModelScope.launch {
            keyword.value = searchKeyword
            userCurrentPage = 1
            postCurrentPage = 1

            try {
                val result = repository.searchAll(searchKeyword)
                posts.value = result.posts ?: emptyList()
                users.value = result.users ?: emptyList()
            } catch (e: Exception) {
                Log.e("Search", "Search failed: ${e.message}")
            }
        }
    }

    // 유저 더 가져오기 (Paging)
    fun fetchMoreUsers() {
        if (isUserLoading) return
        viewModelScope.launch {
            isUserLoading = true
            userCurrentPage++
            try {
                val result = repository.searchAll(
                    keyword = keyword.value,
                    type = "user",
                    currentIndex = userCurrentPage
                )
                val newUsers = result.users ?: emptyList()
                users.update { current -> current + newUsers }
            } finally {
                isUserLoading = false
            }
        }
    }

    // 게시물 더 가져오기 (Paging)
    fun fetchMorePosts() {
        if (isPostLoading) return
        viewModelScope.launch {
            isPostLoading = true
            postCurrentPage++
            try {
                val result = repository.searchAll(
                    keyword = keyword.value,
                    type = "post",
                    currentIndex = postCurrentPage
                )
                val newPosts = result.posts ?: emptyList()
                posts.update { current -> current + newPosts }
            } finally {
                isPostLoading = false
            }
        }
    }

    // 게시물 삭제
    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                repository.deletePost(postId)
                posts.update { list -> list.filterNot { it.id == postId } }
            } catch (e: Exception) {
                Log.e("Search", "Delete failed")
            }
        }
    }

    // 좋아요 토글 (Optimistic UI 적용)
    fun likeToggle(index: Int, postId: Int) {
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: return@launch
            val currentList = posts.value.toMutableList()
            val target = currentList[index]
            val isCurrentlyLiked = target.isLiked ?: false

            // 1. UI 선반영
            val updatedTarget = target.copy(
                isLiked = !isCurrentlyLiked,
                likesCount = if (isCurrentlyLiked) target.likesCount - 1 else target.likesCount + 1
            )
            currentList[index] = updatedTarget
            posts.value = currentList

            // 2. 서버 통신
            try {
                if (isCurrentlyLiked) {
                    repository.cancelLike(postId, userId)
                } else {
                    repository.insertLike(postId, userId)
                }
            } catch (e: Exception) {
                // 3. 실패 시 롤백
                posts.value = posts.value.toMutableList().also { it[index] = target }
            }
        }
    }

    //댓글 기능
    fun fetchComments(id: Int) {
        viewModelScope.launch {
            try {
                selectPostId.value = id
                selectPostId.value?.let { it ->
                    val commentsList: List<CommentWithProfileEntity> = repository.fetchComments(it)
                    comments.value = commentsList
                }
            } catch (e: Exception) {
                Log.d(Constants.TAG, "fetchComments Faile: $e")
            }
        }
    }

    fun unSelectPostId() {
        selectPostId.value = null
        comments.value = listOf()
        userCommentInput.value=""
    }

    fun updateUserInput(input: String){
        userCommentInput.value=input
    }

    fun insertComment() {
        Log.d(Constants.TAG, "insertComment 호출됨 ")
        viewModelScope.launch {
            val postId = selectPostId.value
            if (postId == null) return@launch
            try {
                val userId =
                    SupabaseManager.supabase.auth.currentUserOrNull()?.id
                        ?: return@launch
                if (userCommentInput.value.isNotBlank()){
                    selectPostId.value?.let { it ->
                        repository.insertComment(it, userId, userCommentInput.value)
                        Log.d(Constants.TAG, "insertComment 처리함 ")
                    }

                }

            } catch (e: Exception){
                Log.d(Constants.TAG, "insertComment Error: $e")
                return@launch
            }
            userCommentInput.value=""
            fetchComments(postId)
            Log.d(Constants.TAG, "insertComment 처리 후처리 ")
        }

    }
}
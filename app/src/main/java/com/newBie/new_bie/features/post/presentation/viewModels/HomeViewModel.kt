package com.newBie.new_bie.features.post.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.core.utils.OrderByType
import com.newBie.new_bie.core.utils.PageType
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository : PostRepository = PostRepositoryImpl()
    val pageType : PageType = PageType.HOME
    var posts : MutableStateFlow<List<PostWithProfileEntity>> = MutableStateFlow<List<PostWithProfileEntity>>(listOf())
    var buttonIsWorking : MutableStateFlow<Boolean> = MutableStateFlow(false)
    var type : MutableStateFlow<OrderByType> = MutableStateFlow(OrderByType.NEW_FIRST)

    var currentPage : MutableStateFlow<Int> = MutableStateFlow(1)
    var selectCategory : MutableStateFlow<String> = MutableStateFlow("전체")
    var categoryList : MutableStateFlow<List<String>> = MutableStateFlow(listOf("전체"))

    var isRefreshing = MutableStateFlow(false)


    private var isLoading = false
    private var isLastPage = false

    init {
        getCategoryList()
        fetchPosts()
    }

    // 🔥 정렬 타입 변환
    private fun setOrderBy(type: OrderByType): String {
        return when (type) {
            OrderByType.NEW_FIRST -> "created_at.desc"
            OrderByType.OLD_FIRST -> "created_at.asc"
            OrderByType.LIKES_FIRST -> "likes_count.desc"
        }
    }

    // ✅ 최초 로딩
    fun fetchPosts() {
        viewModelScope.launch {
            val orderBy = setOrderBy(type.value)

            try{
                val result = repository.fetchPosts(
                    orderBy = orderBy,
                    currentIndex = currentPage.value,
                    category = selectCategory.value
                )

                posts.value = result
            } catch (e : Exception) {
                Log.d(Constants.TAG, "fetchPosts 에러 : ${e} ")
            }
        }
    }

    // ✅ 다음 페이지
    fun fetchNextPosts() {
        if (isLoading || isLastPage) return
        viewModelScope.launch {
            isLoading = true
            currentPage.value += 1

            val orderBy = setOrderBy(type.value)

            val newPosts = repository.fetchPosts(
                orderBy = orderBy,
                currentIndex = currentPage.value,
                category = selectCategory.value
            )

            if (newPosts.isEmpty()) {
                isLastPage = true
            } else {
                posts.update { old -> old + newPosts }
            }

            isLoading = false
        }
    }

    // ✅ 리프레시
    fun refresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            type.value = OrderByType.NEW_FIRST
            currentPage.value = 1
            isLastPage = false
            posts.value = emptyList()

            val orderBy = setOrderBy(type.value)

            try{
                posts.value = repository.fetchPosts(
                    orderBy = orderBy,
                    currentIndex = currentPage.value,
                    category = selectCategory.value
                )
            } catch (e : Exception) {
                Log.e(Constants.TAG, "Refresh Error: $e")
            } finally {
                isRefreshing.value = false
            }
        }
    }

    // ✅ 카테고리 변경
    fun changeCategory(category: String) {
        viewModelScope.launch {
            selectCategory.value = category
            currentPage.value = 1
            posts.value = emptyList()

            fetchPosts()
        }
    }


    // ✅ 정렬 변경
    fun changeOrder(inputType: OrderByType) {
        viewModelScope.launch {
            type.value = inputType
            currentPage.value = 1
            posts.value = emptyList()

            fetchPosts()
        }
    }


    // ✅ 카테고리 리스트
    private fun getCategoryList() {
        viewModelScope.launch {
            try{
                val categories = repository.getCategoryList()
                categoryList.value = listOf("전체") + categories
            } catch (e : Exception) {
                Log.d(Constants.TAG, "getCategoryList 에러 : ${e} ")
            }
        }
    }

    // ✅ 게시글 삭제
    fun deletePost(postId: Int) {
        viewModelScope.launch {
            repository.deletePost(postId)

            posts.update { list ->
                list.filterNot { it.id == postId }
            }
        }
    }

    // ✅ 좋아요 토글 (Optimistic UI)
    fun likeToggle(index: Int, postId: Int) {
        viewModelScope.launch {

            val userId =
                SupabaseManager.supabase.auth.currentUserOrNull()?.id
                    ?: return@launch

            val currentList = posts.value.toMutableList()
            val target = currentList[index]

            target.isLiked?.let {
                if (!it) {
                    // 👍 좋아요
                    currentList[index] =
                        target.copy(
                            isLiked = true,
                            likesCount = target.likesCount + 1
                        )
                    posts.value = currentList

                    try {
                        repository.insertLike(postId, userId)
                    } catch (e: Exception) {
                        // 롤백
                        currentList[index] =
                            target.copy(
                                isLiked = false,
                                likesCount = target.likesCount
                            )
                        posts.value = currentList
                    }

                } else {
                    // ❌ 좋아요 취소
                    currentList[index] =
                        target.copy(
                            isLiked = false,
                            likesCount = target.likesCount - 1
                        )
                    posts.value = currentList

                    try {
                        repository.cancelLike(postId, userId)
                    } catch (e: Exception) {
                        // 롤백
                        currentList[index] =
                            target.copy(
                                isLiked = true,
                                likesCount = target.likesCount
                            )
                        posts.value = currentList
                    }
                }
            }
        }
    }

}
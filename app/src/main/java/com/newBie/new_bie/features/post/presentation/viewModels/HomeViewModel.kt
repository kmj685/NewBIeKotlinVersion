package com.newBie.new_bie.features.post.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.core.utils.OrderByType
import com.newBie.new_bie.core.utils.PageType
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import com.newBie.new_bie.features.post.presentation.interfaces.CommentBottomSheetViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(), CommentBottomSheetViewModel {

    private val repository : PostRepository = PostRepositoryImpl()
    val pageType : PageType = PageType.HOME
    var posts : MutableStateFlow<List<PostWithProfileEntity>> = MutableStateFlow<List<PostWithProfileEntity>>(listOf())
    override var comments : MutableStateFlow<List<CommentWithProfileEntity>> = MutableStateFlow<List<CommentWithProfileEntity>>(listOf())
    override var selectPostId : MutableStateFlow<Int?> = MutableStateFlow(null)
    override val selectCommentId: MutableStateFlow<Int?> = MutableStateFlow(null)

    var buttonIsWorking : MutableStateFlow<Boolean> = MutableStateFlow(false)
    var type : MutableStateFlow<OrderByType> = MutableStateFlow(OrderByType.NEW_FIRST)

    var currentPage : MutableStateFlow<Int> = MutableStateFlow(1)
    var selectCategory : MutableStateFlow<String> = MutableStateFlow("전체")
    var categoryList : MutableStateFlow<List<String>> = MutableStateFlow(listOf("전체"))

    var isRefreshing = MutableStateFlow(false)

    override var userCommentInput: MutableStateFlow<String> = MutableStateFlow("")
    override val editUserCommentInput: MutableStateFlow<String> = MutableStateFlow("")


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
                        Log.d(Constants.TAG, "likeToggle 좋아요 시도")
                    } catch (e: PostgrestRestException) {
                        Log.d(Constants.TAG, "likeToggle 좋아요 실패  : ${e}")
                        // 롤백
                        if (e.code =="23505") {
                            repository.cancelLike(postId, userId)
                        }
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
                        Log.d(Constants.TAG, "likeToggle 좋아요 취소 시도")
                    } catch (e: Exception) {
                        Log.d(Constants.TAG, "likeToggle 좋아요 취소 실패  : ${e}")
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

    override fun unSelectPostId() {
        selectPostId.value = null
        comments.value = listOf()
        userCommentInput.value=""
        editUserCommentInput.value=""
        selectCommentId.value=null
    }

    override fun updateUserInput(input: String){
        userCommentInput.value=input
    }

    override fun updateEditUserInput(input: String) {
        editUserCommentInput.value=input
    }

    override fun insertComment() {
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

    override fun deleteComment(id: Int,authorId: String) {
        viewModelScope.launch {
            try{
                val postId = selectPostId.value
                if (postId == null) return@launch
                val userId =
                    SupabaseManager.supabase.auth.currentUserOrNull()?.id
                        ?: return@launch
                if (authorId != userId) return@launch
                repository.deleteComment(id)
            }catch (e: Exception){
                return@launch
            }
            val index = comments.value.indexOfFirst { it.id == id }
            if (index != -1){
                val newComment : MutableList<CommentWithProfileEntity> = comments.value.toMutableList()
                newComment.removeAt(index)
                comments.value = newComment
            }
        }

    }

    override fun onSelectComment(commentId: Int, content: String) {
        selectCommentId.value=commentId
        editUserCommentInput.value=content
    }

    override fun editComment(authorId: String) {
        viewModelScope.launch {
            val commentId = selectCommentId.value
            val userId =
                SupabaseManager.supabase.auth.currentUserOrNull()?.id
                    ?: return@launch
            if (commentId == null || userId!= authorId || editUserCommentInput.value.isBlank() || selectPostId.value == null) return@launch
            try {
                Log.d(Constants.TAG, "editComment 실행")
                repository.editComment(commentId, content = editUserCommentInput.value)
                Log.d(Constants.TAG, "editComment 실행 성공")
            }catch (e: Exception){
                Log.d(Constants.TAG, "editComment 실패: $e")
                return@launch
            }
        }
        selectCommentId.value=null
        editUserCommentInput.value=""
        selectPostId.value?.let { fetchComments(it) }
    }

    override fun onCancel() {
        editUserCommentInput.value = ""
        selectCommentId.value = null
    }

}
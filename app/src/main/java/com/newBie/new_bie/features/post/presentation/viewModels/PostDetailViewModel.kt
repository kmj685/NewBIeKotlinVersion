package com.newBie.new_bie.features.post.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.PostImageEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import com.newBie.new_bie.features.post.presentation.interfaces.CommentBottomSheetViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel : ViewModel(), CommentBottomSheetViewModel {
    private val repository : PostRepository = PostRepositoryImpl()
    var post : MutableStateFlow<PostWithProfileEntity?> = MutableStateFlow(null)
    override var selectPostId : MutableStateFlow<Int?> = MutableStateFlow(null)

    override var comments : MutableStateFlow<List<CommentWithProfileEntity>> = MutableStateFlow<List<CommentWithProfileEntity>>(listOf())
    override val selectCommentId: MutableStateFlow<Int?> = MutableStateFlow(null)
    override var userCommentInput: MutableStateFlow<String> = MutableStateFlow("")
    override val editUserCommentInput: MutableStateFlow<String> = MutableStateFlow("")


    var images : MutableStateFlow<List<PostImageEntity>> = MutableStateFlow(listOf())
    fun fetchPost(id : Int) {
        viewModelScope.launch {
            val result =repository.fetchPostItem(id)
            if (result == null)return@launch
            post.value = result
            images.value = result.postImages
        }
    }

    fun likeToggle() {
        Log.d(Constants.TAG, "PostDetailViewModel.likeToggle() 호출됨")

        viewModelScope.launch {
            Log.d(Constants.TAG, "userId : ${SupabaseManager.supabase.auth.currentUserOrNull()?.id}")

            val userId =
                SupabaseManager.supabase.auth.currentUserOrNull()?.id
                    ?: return@launch


            Log.d(Constants.TAG, "isLike : ${post.value?.isLiked}")
            if(post.value?.isLiked == false) {
                try {
                    post.value?.id?.let {
                        repository.insertLike(it, userId)
                        val targetPost = post.value?.likesCount
                        post.value.let{
                            post.value = it?.copy(
                                isLiked = true,
                                likesCount = it.likesCount + 1
                            )
                        }
                        Log.d(Constants.TAG, "좋아요 후 isLike 값 : ${post.value?.isLiked}")
                    }

                } catch (e : Exception) {
                    Log.d(Constants.TAG, "좋아요 실패 : ${e}")
                    post.value.let{
                        post.value = it?.copy(
                            isLiked = false,
                            likesCount = it.likesCount - 1
                        )
                    }
                }
            } else {
                try {
                    post.value?.let {
                        repository.cancelLike(it.id, userId)
                        post.value.let{
                            post.value = it?.copy(
                                isLiked = false,
                                likesCount = it.likesCount - 1
                            )
                        }
                        Log.d(Constants.TAG, "좋아요 취소 후 isLike 값 : ${post.value?.isLiked}")
                    }

                } catch (e : Exception) {
                    Log.d(Constants.TAG, "좋아요 취소 실패 : ${e}")
                    post.value.let{
                        post.value = it?.copy(
                            isLiked = true,
                            likesCount = it.likesCount + 1
                        )
                    }
                }
            }
        }
    }

    override fun updateUserInput(input: String){
        userCommentInput.value=input
    }

    override fun updateEditUserInput(input: String) {
        editUserCommentInput.value=input
    }
    fun fetchComments() {
        viewModelScope.launch {
            try {
                val id = post.value?.id
                if (id == null) return@launch
                val commentsList: List<CommentWithProfileEntity> = repository.fetchComments(id)
                comments.value = commentsList
            } catch (e: Exception) {
                Log.d(Constants.TAG, "fetchComments Faile: $e")
            }
        }
    }

    override fun insertComment() {
        Log.d(Constants.TAG, "insertComment 호출됨 ")
        viewModelScope.launch {
            val postId = post.value?.id
            if (postId == null) return@launch
            try {
                val userId =
                    SupabaseManager.supabase.auth.currentUserOrNull()?.id
                        ?: return@launch
                if (userCommentInput.value.isNotBlank()){
                    repository.insertComment(postId, userId, userCommentInput.value)
                    Log.d(Constants.TAG, "insertComment 처리함 ")
                }
            } catch (e: Exception){
                Log.d(Constants.TAG, "insertComment Error: $e")
                return@launch
            }
            userCommentInput.value=""
            fetchComments()
            Log.d(Constants.TAG, "insertComment 처리 후처리 ")
        }
    }

    override fun deleteComment(id: Int, authorId: String) {
        viewModelScope.launch {
            try{
                val postId = post.value?.id
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
            if (commentId == null || userId!= authorId || editUserCommentInput.value.isBlank() || post.value?.id == null) return@launch
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
        fetchComments()
    }

    override fun onCancel() {
        editUserCommentInput.value = ""
        selectCommentId.value = null
    }

    override fun unSelectPostId() {
        selectPostId.value = null
        comments.value = listOf()
        userCommentInput.value=""
        editUserCommentInput.value=""
        selectCommentId.value=null
    }

}
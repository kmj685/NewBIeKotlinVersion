package com.newBie.new_bie.features.post.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel: ViewModel() {
    private val repository : PostRepository = PostRepositoryImpl()
    var post : MutableStateFlow<PostWithProfileEntity?> = MutableStateFlow(null)


    fun fetchPost(id : Int) {
        viewModelScope.launch {
            post.value = repository.fetchPostItem(id)
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
}
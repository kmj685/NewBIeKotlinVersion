package com.newBie.new_bie.features.post.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
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
}
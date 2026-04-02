package com.newBie.new_bie.features.post.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.CategoryEntity
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PostAddViewModel : ViewModel() {
    private val repository : PostRepository = PostRepositoryImpl()
    var titleInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var contentInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var categoryList : MutableStateFlow<List<CategoryTypeEntity>> = MutableStateFlow(listOf())
    var selectCategoryList : MutableStateFlow<List<CategoryTypeEntity>> = MutableStateFlow(listOf())

    init {
        getCategoryList()
    }

    fun onChangeTitleInput(title : String) {
        titleInputTxt.value = title
    }

    fun onChangeContentInput(content : String) {
        contentInputTxt.value = content
    }

    // ✅ 카테고리 리스트
    private fun getCategoryList() {
        viewModelScope.launch {
            try{
                val categories = repository.getCategoryTypeList()
                categoryList.value = categories
            } catch (e : Exception) {
                Log.d(Constants.TAG, "getCategoryList 에러 : ${e} ")
            }
        }
    }

    fun selectCategory(category : CategoryTypeEntity) {
        selectCategoryList.value += category
    }

    fun unselectCategory(category : CategoryTypeEntity) {
        selectCategoryList.value -= category
    }

    fun toggleCategory(category: CategoryTypeEntity) {
        val current = selectCategoryList.value

        selectCategoryList.value =
            if (current.contains(category)) {
                current - category
            } else {
                current + category
            }
    }

    fun insertPost() {
        val selectCategoryIds : List<Int> = selectCategoryList.value.map { item ->
            item.id
        }

    }
}
package com.newBie.new_bie.features.post.presentation.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostEditViewModel : ViewModel() {
    private val repository : PostRepository = PostRepositoryImpl()
    var titleInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var contentInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var categoryList : MutableStateFlow<List<CategoryTypeEntity>> = MutableStateFlow(listOf())
    var selectCategoryList : MutableStateFlow<List<CategoryTypeEntity>> = MutableStateFlow(listOf())

    private val _imageInputList = MutableStateFlow<List<Uri>>(listOf())
    val imageInputList = _imageInputList.asStateFlow()

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
    // 이미지 선택
    fun getImage(selectImagesList: List<Uri>){
        _imageInputList.update { currentList ->
            val newList = currentList + selectImagesList

            Log.d(TAG, "방금 추가된 사진 개수: ${selectImagesList.size}")
            Log.d(TAG, "현재 선택된 전체 사진 리스트 ${newList}")
            Log.d(TAG, "총 사진 개수: ${newList.size}")

            newList
        }
    }
    // 이미지 선택 취소
    fun deleteImage(selectImages: Uri){

        _imageInputList.update { currentList ->
            val newList = currentList - selectImages

            newList// _imageInputList.value = _imageInputList.value - selectImages
        }
    }

}
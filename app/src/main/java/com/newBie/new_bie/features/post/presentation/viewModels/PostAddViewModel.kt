package com.newBie.new_bie.features.post.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.CategoryEntity
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PostAddViewModel : ViewModel() {
    private val repository : PostRepository = PostRepositoryImpl()
    var titleInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var contentInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var categoryList : MutableStateFlow<List<CategoryTypeEntity>> = MutableStateFlow(listOf())
    var selectCategoryList : MutableStateFlow<List<CategoryTypeEntity>> = MutableStateFlow(listOf())

    var imageInputList : MutableStateFlow<List<String>> = MutableStateFlow(listOf())

    // 게시물 등록 성공 이벤트를 보낼 Flow
    private val _postSuccess = MutableSharedFlow<Boolean>()
    val postSuccess = _postSuccess.asSharedFlow()

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
    fun getImage(selectImagesList: List<String>){
        imageInputList.value = imageInputList.value + selectImagesList
    }
    // 이미지 선택 취소 나중에 사용하쇼ㅋ
    fun deleteImage(selectImages: String){
        imageInputList.value = imageInputList.value - selectImages
    }

    // 게시글 등록
    fun insertPost(){
        viewModelScope.launch {

            val userID = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: return@launch //안되면 함수 끝냄

            val selectCategoryIds : List<Int> = selectCategoryList.value.map { item ->
                item.id
            }
            try {
                repository.insertPost(
                    userId = userID,
                    title = titleInputTxt.value,
                    content = contentInputTxt.value,
                    images = imageInputList.value,
                    categories = selectCategoryIds
                )
                // 게시물 등록 성공 방출 StateFlow
                _postSuccess.emit(true)
            } catch (e: HttpException) {
                // 서버에서 내려준 구체적인 에러 메시지를 확인합니다.
                val errorBody = e.response()?.errorBody()?.string()
                Log.e(Constants.TAG, "게시글 등록 HTTP 에러: $errorBody")
            } catch (e: Exception) {
                Log.e(Constants.TAG, "게시글 등록 일반 에러: $e")
            }
        }
    }
}
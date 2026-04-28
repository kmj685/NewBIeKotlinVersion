package com.newBie.new_bie.features.post.presentation.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.PhotoPickerManager
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import com.newBie.new_bie.features.profile.presentation.viewModels.MyProfileViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PostAddViewModel : ViewModel() {
    private val repository : PostRepository = PostRepositoryImpl()
    var titleInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var contentInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var categoryList : MutableStateFlow<List<CategoryTypeEntity>> = MutableStateFlow(listOf())

    //최종 추가할 완료 카테고리 리스트
    var selectCategoryList : MutableStateFlow<List<CategoryTypeEntity>> = MutableStateFlow(listOf())
    // 바텀 시트에 체크 된 카테고리 임시로 체크 중인 리스트
    private val _bottomSheetSelectedCategories = MutableStateFlow<List<CategoryTypeEntity>>(listOf())
    val bottomSheetSelectedCategories = _bottomSheetSelectedCategories.asStateFlow()

    private val _imageInputList = MutableStateFlow<List<Uri>>(listOf())
    val imageInputList = _imageInputList.asStateFlow()

    // 게시물 등록 성공 이벤트를 보낼 Flow
    private val _postSuccess = MutableSharedFlow<Boolean>()
    val postSuccess = _postSuccess.asSharedFlow()

    // 게시물 등록 버튼 인디케이터
    private val _isPosting = MutableStateFlow<Boolean>(false)
    val isPosting = _isPosting.asStateFlow()

    init {
        getCategoryList()
    }

    fun onChangeTitleInput(title : String) {
        titleInputTxt.value = title
    }

    fun onChangeContentInput(content : String) {
        contentInputTxt.value = content
    }

    // 카테고리 리스트
    private fun getCategoryList() {
        viewModelScope.launch {
            try{
                val categories = repository.getCategoryTypeList()
                categoryList.value = categories
            } catch (e : Exception) {
                Log.d(TAG, "getCategoryList 에러 : ${e} ")
            }
        }
    }

    // 바텀 시트를 열 때 확정된 리스트를 임시 보관함에 복사
    fun openBottomSheetCopyCategoriesList(){
        _bottomSheetSelectedCategories.value = selectCategoryList.value
    }

    fun unselectCategory(category : CategoryTypeEntity) {
        selectCategoryList.value -= category
    }

    fun toggleCategory(category: CategoryTypeEntity) {
        val current = _bottomSheetSelectedCategories.value

        _bottomSheetSelectedCategories.value =
            if (current.contains(category)) {
                current - category
            } else {
                current + category
            }
    }

    // 완료 버튼 눌렀을 때 임시 보관함을 실제 리스트로 확정
    fun confirmSelection() {
        selectCategoryList.value = _bottomSheetSelectedCategories.value
    }

    // 선택 헤제 눌렀을 때 임시 보관함 비우기
    fun clearCategories(){
        _bottomSheetSelectedCategories.value = emptyList()
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

    // 게시글 등록
    fun insertPost(context: Context){
        // Activity Context 대신 생명주기가 긴 Application Context로 변환
        val appContext = context.applicationContext
        viewModelScope.launch {

            val userID = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: return@launch //안되면 함수 끝냄

            val selectCategoryIds : List<Int> = selectCategoryList.value.map { item ->
                item.id
            }
            try {
                _isPosting.value = true

                val uploadedImageUrls = PhotoPickerManager.uploadImages(
                    context = appContext,
                    uriList = _imageInputList.value,
                    userId = userID,
                    pathName = "post"
                )

                repository.insertPost(
                    userId = userID,
                    title = titleInputTxt.value,
                    content = contentInputTxt.value,
                    images = uploadedImageUrls,
                    categories = selectCategoryIds
                )
                // 게시물 등록 성공 방출 StateFlow
                _postSuccess.emit(true)

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e(TAG, "게시글 등록 HTTP 에러: $errorBody")
            } catch (e: Exception) {
                Log.e(TAG, "게시글 등록 일반 에러: $e")
            } finally {
                _isPosting.value = false
            }
        }
    }
}
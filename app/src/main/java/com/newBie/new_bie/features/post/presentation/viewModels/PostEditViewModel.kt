package com.newBie.new_bie.features.post.presentation.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.PhotoPickerManager
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.collections.plus
import androidx.core.net.toUri

class PostEditViewModel : ViewModel() {
    private val repository : PostRepository = PostRepositoryImpl()
    var currentPostId: Int? = null
    var titleInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var contentInputTxt: MutableStateFlow<String> = MutableStateFlow("")
    var categoryList : MutableStateFlow<List<CategoryTypeEntity>> = MutableStateFlow(listOf())
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

    fun fetchPostItem(id: Int){
        viewModelScope.launch {
            val result = repository.fetchPostItem(id) ?:return@launch

            currentPostId = id

            titleInputTxt.value = result.title ?:""
            contentInputTxt.value = result.content ?:""
            _imageInputList.value = result.postImages.map { it.imageUrl.toUri() }
            selectCategoryList.value = result.categories.map { it.categoryType }
        }
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

                val currentImages = _imageInputList.value

                // 🔍 [핵심 로직] 기존 이미지와 새 이미지 분리하기
                // 기기에서 새로 가져온 이미지는 스키마가 content:// 혹은 file:// 입니다.
                // 이미 서버에 저장된 주소는 http:// 혹은 https:// 로 시작합니다.
                val existingUrls = currentImages.filter { it.scheme == "http" || it.scheme == "https" }.map { it.toString() }
                val newLocalUris = currentImages.filter { it.scheme != "http" && it.scheme != "https" }

                // 새로 추가된 사진만 스토리지에 업로드
                val newUploadedUrls = if (newLocalUris.isNotEmpty()) {
                    PhotoPickerManager.uploadImages(
                        context = appContext,
                        uriList = newLocalUris,
                        userId = userID,
                        pathName = "post"
                    )
                } else {
                    emptyList()
                }

                // 기존 서버 주소와 새로 업로드된 주소를 합쳐 최종 데이터 리스트 생성
                val finalImageUrls = existingUrls + newUploadedUrls

                if (currentPostId == null) {
                    // 1) 신규 등록 상태일 때
                    repository.insertPost(
                        userId = userID,
                        title = titleInputTxt.value,
                        content = contentInputTxt.value,
                        images = finalImageUrls,
                        categories = selectCategoryIds
                    )
                } else {
                    // 2) 기존 게시글 수정 상태일 때 (Repository에 updatePost가 구현되어 있어야 합니다)
                    repository.updatePost(
                        postId = currentPostId!!,
                        title = titleInputTxt.value,
                        content = contentInputTxt.value,
                        images = finalImageUrls,
                        categories = selectCategoryIds
                    )
                }
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
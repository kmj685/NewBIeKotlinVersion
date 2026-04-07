package com.newBie.new_bie.features.post.presentation.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.components.uriToByteArray
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    var imageInputList : MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())

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
        imageInputList.value = imageInputList.value + selectImagesList
    }
    // 이미지 선택 취소 나중에 사용하쇼ㅋ
    fun deleteImage(selectImages: Uri){
        imageInputList.value = imageInputList.value - selectImages
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

                // core에 있는 uriToByteArray 함수를 사용해서 ByteArray로 자료형을 바꿔준다.
                // 갤러리에 접근하려면 ContentResolver라는 권한이 필요한데 이 때 context(신분증 같은 역할)가 필요하다네요
                val byteArrayList = imageInputList.value.mapNotNull { uri ->
                    uriToByteArray(appContext, uri)
                }

                // 우리가 알고 있는 http:// .... 로 path를 바꿔주는 작업
                // index를 붙이는 이유는 중복방지와 사진을 여러 장 올릴 경우에 순서가 필요하니까
                val uploadedImageUrls = byteArrayList.mapIndexed { index, bytes ->
                    val fileName = "private/post_${userID}_${System.currentTimeMillis()}_$index.png"

                    // supabase storage의 "images"에 httpL//로 바꿨던 주소를 업로드하는 작업
                    SupabaseManager.supabase.storage.from("images").upload(
                        path = fileName,
                        data = bytes,
                        options = {
                            upsert = false //이미 같은 이름의 파일이 있으면 false = 실퍃시켜라 true = 덮어쓰기"
                        }
                    )
                    // 업로드했던 이미지의 주소를 가져오는 작업 -> 이제 밑에 보면 images에 넣어줘야지 사진이 올라가니까
                    SupabaseManager.supabase.storage.from("images").publicUrl(fileName)
                }.filterNotNull()
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
            }
        }
    }
}
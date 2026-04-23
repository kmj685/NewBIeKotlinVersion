package com.newBie.new_bie.features.profile.presentation.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.PhotoPickerManager
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.post.data.repositories.PostRepositoryImpl
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UpdateProfileViewModel: ViewModel() {

    private val repository : PostRepository = PostRepositoryImpl()


    // 유저 정보
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    // 닉네임
    val nicknameState = TextFieldState(_user.value?.nickName ?:"")
    // 자기소개
    val introductionState = TextFieldState(_user.value?.introduction ?:"")
    // 프로필 사진
    private val _imageInput = MutableStateFlow<Uri?>(null)
    val imageInput: StateFlow<Uri?> = _imageInput.asStateFlow()

    // 프로필 업데이트 성공 이벤트 보낼 Flow
    private val _updateSuccess = MutableSharedFlow<Boolean>()
    val updateSuccess: SharedFlow<Boolean> = _updateSuccess.asSharedFlow()

    // 삭제 여부 상태
    private val _isImageDeleted = MutableStateFlow<Boolean>(false)
    val isImageDeleted = _isImageDeleted.asStateFlow()

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile(){
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?:return@launch

            val fetchedUser = repository.fetchUser(userId)

            _user.value = fetchedUser

            // 업데이트 해야한다. 시점의 차이 때문에 nickName을 null로 가져오기 때문
            fetchedUser?.nickName?.let{ name ->
                nicknameState.edit {
                    replace(0, length, name)
                }
            }

            // 자기소개도 마찬가지 같은 원리니까
            fetchedUser?.introduction?.let{ introduction ->
                introductionState.edit {
                    replace(0, length, introduction)
                }
            }
        }
    }

    fun getImage(selectImage: Uri){
        _imageInput.value = selectImage
        _isImageDeleted.value = false
    }

    fun deleteImage(){
        _imageInput.value = null
        _isImageDeleted.value = true
    }

    fun saveUserProfile(context: Context){
        // Activity Context 대신 생명주기가 긴 Application Context로 변환
        val appContext = context.applicationContext

        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
                ?:return@launch

            try {
                val currentUri = _imageInput.value

                val uploadImageUrl =

                    if (currentUri != null ) {
                        PhotoPickerManager.uploadSingleImage(
                            context = appContext,
                            uri = _imageInput.value,
                            userId = userId,
                            pathName = "profile"
                        )
                    } else if(_isImageDeleted.value == true) {
                        null
                    } else {
                        _user.value?.profileImage
                    }


                repository.updateUserProfile(
                    userId = userId,
                    image = uploadImageUrl,
                    nickname = nicknameState.text.toString(),
                    introduction = introductionState.text.toString()
                )

                // 업데이트 등록 성공 방출 StateFlow
                _updateSuccess.emit(true)

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e(TAG, "프로필 업데이트 HTTP 에러: $errorBody")
            } catch (e: Exception) {
                Log.e(TAG, "프로필 업데이트 일반 에러: $e")
            }
        }
    }
}
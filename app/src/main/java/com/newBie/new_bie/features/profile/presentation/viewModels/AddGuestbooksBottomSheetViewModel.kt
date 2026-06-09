package com.newBie.new_bie.features.profile.presentation.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.PhotoPickerManager
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.usecase.guestbooksUseCase.InsertGuestbookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class AddGuestbooksBottomSheetViewModel @Inject constructor(
    private val saveStateHandle: SavedStateHandle, // Hilt가 알아서 넣어준다.
    private val insertGuestbookUseCase: InsertGuestbookUseCase
): ViewModel() {

    private val _targetUserId = MutableStateFlow<String?>(saveStateHandle.get<String>("userId"))

    val userContentInput = TextFieldState()

    // 이미지
    private val _imageInput = MutableStateFlow<Uri?>(null)
    val imageInput: StateFlow<Uri?> = _imageInput.asStateFlow()

    // 게시물 등록 성공 이벤트를 보낼 Flow
    private val _guestbookSaveSuccess = MutableSharedFlow<Boolean>()
    val guestbookSaveSuccess = _guestbookSaveSuccess.asSharedFlow()

    // 등록 버튼 인디케이터
    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving = _isSaving.asStateFlow()

    // 이미지 선택
    fun getImage(selectImage: Uri){
        _imageInput.value = selectImage
    }
    // 이미지 선택 취소
    fun deleteImage(){
        _imageInput.value = null
    }

    // 방명록 등록
    fun saveGuestbook(context: Context){

        viewModelScope.launch {
            val receiverId = _targetUserId.value ?:return@launch
            val senderId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch

            _isSaving.value = true

            val uploadImage = PhotoPickerManager.uploadSingleImage(
                context = context,
                uri = _imageInput.value,
                userId = senderId,
                pathName = "guestbooks"
            )

            val result = insertGuestbookUseCase(
                receiverId = receiverId,
                senderId = senderId,
                content = userContentInput.text.toString(),
                image = uploadImage
            )

            _isSaving.value = false

            result.onSuccess {
                _imageInput.value = null
                userContentInput.clearText()
                _guestbookSaveSuccess.emit(true)
            }.onFailure {e ->
                Log.e(TAG, "saveGuestbook: ${e.message}", )
            }
        }
    }
}
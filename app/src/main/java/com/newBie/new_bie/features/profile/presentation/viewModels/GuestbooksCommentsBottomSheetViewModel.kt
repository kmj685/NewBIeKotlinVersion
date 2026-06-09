package com.newBie.new_bie.features.profile.presentation.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.entities.GuestbooksCommentsEntity
import com.newBie.new_bie.features.profile.domain.usecase.guestbooksCommentsUseCase.DeletedGuestbooksCommentUseCase
import com.newBie.new_bie.features.profile.domain.usecase.guestbooksCommentsUseCase.GetGuestbooksCommentsListUseCase
import com.newBie.new_bie.features.profile.domain.usecase.guestbooksCommentsUseCase.InsertGuestbooksCommentUseCase
import com.newBie.new_bie.features.profile.domain.usecase.guestbooksCommentsUseCase.UpdatedGuestbooksCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuestbooksCommentsBottomSheetViewModel @Inject constructor(
    private val saveStateHandle: SavedStateHandle, // Hilt가 알아서 넣어준다.
    private val getGuestbooksCommentsListUseCase: GetGuestbooksCommentsListUseCase,
    private val insertGuestbooksCommentUseCase: InsertGuestbooksCommentUseCase,
    private val deletedGuestbooksCommentUseCase: DeletedGuestbooksCommentUseCase,
    private val updatedGuestbooksCommentUseCase: UpdatedGuestbooksCommentUseCase
): ViewModel() {
    // 상세 페이지의 postId, guestbookId처럼 "이 값 없으면 화면을 그리는 것 자체가 불가능한 필수 고정값"을 다룰 때
    private val guestbookId: Int = checkNotNull(saveStateHandle["guestbookId"])

    private val _guestbooksComments = MutableStateFlow<List<GuestbooksCommentsEntity?>>(emptyList())
    val guestbooksComments = _guestbooksComments.asStateFlow()
    private var _selectGuestbooksId = MutableStateFlow<Int?>(null)
    var selectGuestbooksId = _selectGuestbooksId.asStateFlow()
    private val _selectCommentId = MutableStateFlow<Int?>(null)
    val selectCommentId = _selectCommentId.asStateFlow()
    private var _userCommentInput = MutableStateFlow<String>("")
    var userCommentInput = _userCommentInput.asStateFlow()
    private var _editUserCommentInput = MutableStateFlow<String>("")
    var editUserCommentInput = _editUserCommentInput.asStateFlow()

    init {
        getGuestbooksComments()
    }

    fun getGuestbooksComments(){
        viewModelScope.launch {
            val result = getGuestbooksCommentsListUseCase(guestbookId = guestbookId)

            result.onSuccess {
                _guestbooksComments.value = it
            }.onFailure { e ->
                Log.e(TAG, "getGuestbooksComments: ${e.message}")
                _guestbooksComments.value = emptyList()
            }
        }
    }
    fun unSelectPostId() {
        _selectGuestbooksId.value = null
        _guestbooksComments.value = listOf()
        _userCommentInput.value=""
        _editUserCommentInput.value=""
        _selectCommentId.value=null
    }
    fun updateUserInput(input: String){
        _userCommentInput.value = input
    }
    fun updateEditUserInput(input: String) {
        _editUserCommentInput.value = input
    }
    fun insertComment() {
        viewModelScope.launch {
            val guestbooksId = guestbookId
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch

            val result = insertGuestbooksCommentUseCase(
                guestbookId = guestbooksId,
                userId = userId,
                content = _userCommentInput.value
            )

            result.onSuccess {
                _userCommentInput.value = ""
                getGuestbooksComments()
            }.onFailure {e ->
                Log.e(TAG, "insertComment: ${e.message}", )
            }
        }
    }

    fun deleteComment(commentId: Int, authorId: String) {
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch
            if (authorId != userId) return@launch

            val result = deletedGuestbooksCommentUseCase(
                commentId = commentId
            )

            result.onSuccess { 
                getGuestbooksComments()
            }.onFailure { e ->
                Log.e(TAG, "deleteComment: ${e.message}", )
            }
        }
    }
    fun onSelectComment(commentId: Int, content: String) {
        _selectCommentId.value = commentId
        _editUserCommentInput.value = content
    }
    fun editComment(authorId: String) {
        viewModelScope.launch {
            val commentId = _selectCommentId.value ?:return@launch
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch
            if (authorId != userId) return@launch

            val result = updatedGuestbooksCommentUseCase(
                commentId = commentId,
                content = _editUserCommentInput.value
            )

            result.onSuccess {
                onCancel()
                getGuestbooksComments()
            }.onFailure { e ->
                Log.e(TAG, "editComment: ${e.message}", )
            }
        }
    }
    fun onCancel() {
        _editUserCommentInput.value = ""
        _selectCommentId.value = null
    }
}
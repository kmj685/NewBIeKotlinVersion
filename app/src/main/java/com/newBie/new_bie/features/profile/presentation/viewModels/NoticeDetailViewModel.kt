package com.newBie.new_bie.features.profile.presentation.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.entities.NoticesEntity
import com.newBie.new_bie.features.profile.domain.usecase.noticesUseCase.GetNoticeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoticeUseCase: GetNoticeUseCase
): ViewModel() {
    private val _notice = MutableStateFlow<NoticesEntity?>(null)
    val notice = _notice.asStateFlow()

    init {
        val noticeId: Int? = savedStateHandle.get<Int>("noticeId")

        noticeId?.let {
            fetchGetNotice(noticeId = noticeId)
        }
    }


    fun fetchGetNotice(noticeId: Int){
        viewModelScope.launch {
            val result = getNoticeUseCase(noticeId = noticeId)
            result.onSuccess {
                _notice.value = it
            }.onFailure {
                Log.e(TAG, "fetchGetNotice: ${it.message}", )
                _notice.value = null
            }
        }
    }
}
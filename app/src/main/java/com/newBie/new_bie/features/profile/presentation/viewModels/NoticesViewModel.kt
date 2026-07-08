package com.newBie.new_bie.features.profile.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.entities.NoticesEntity
import com.newBie.new_bie.features.profile.domain.usecase.noticesUseCase.GetNoticeUseCase
import com.newBie.new_bie.features.profile.domain.usecase.noticesUseCase.GetNoticesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticesViewModel @Inject constructor(
    private val getNoticesListUseCase: GetNoticesListUseCase,
    private val getNoticeUseCase: GetNoticeUseCase
): ViewModel(){
    private val _noticesList = MutableStateFlow<List<NoticesEntity>>(emptyList())
    val noticesList = _noticesList.asStateFlow()
    private val _notice = MutableStateFlow<NoticesEntity?>(null)
    val notice = _notice.asStateFlow()

    init {
        fetchGetNoticesList()
        _notice.value?.id?.let {
            fetchGetNotice(it)
        }
    }

    fun fetchGetNoticesList(){
        viewModelScope.launch {
            val result = getNoticesListUseCase()
            result.onSuccess {
                _noticesList.value = it
            }.onFailure {
                Log.e(TAG, "fetchGetNoticesList: ${it.message}", )
                _noticesList.value = emptyList()
            }
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
package com.newBie.new_bie.features.profile.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.usecase.settingUseCase.GetNotificationStatusUseCase
import com.newBie.new_bie.features.profile.domain.usecase.settingUseCase.SetNotificationEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val setNotificationEnabledUseCase: SetNotificationEnabledUseCase,
    private val getNotificationStatusUseCase: GetNotificationStatusUseCase
): ViewModel() {
    val showDialog = MutableStateFlow<Boolean>(false)
    private val _isNotificationEnabled = MutableStateFlow<Boolean>(true)
    val isNotificationEnabled = _isNotificationEnabled.asStateFlow()

    init {
        fetchNotificationStatus()
    }

    // 알림 설정-해제 토글
    fun setNotificationEnabled(isEnabled: Boolean){
        // UI 즉시 업데이트(낙관적 업데이트)
        _isNotificationEnabled.value = isEnabled

        viewModelScope.launch {
            val currentUserId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch
            val result = setNotificationEnabledUseCase(
                userId = currentUserId,
                isEnabled = isEnabled
            )
            result.onSuccess {
                if (isEnabled){
                    Log.d(TAG, "setNotificationEnabled: 알림 설정")
                } else {
                    Log.d(TAG, "setNotificationEnabled: 알림 해제")
                }
            }.onFailure {
                _isNotificationEnabled.value = !isEnabled
                Log.e(TAG, "setNotificationEnabled: ${it.message}", )
            }
        }
    }

    // 알림 상태
    fun fetchNotificationStatus(){
        viewModelScope.launch {
            val currentUserId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch
            val result = getNotificationStatusUseCase(userId = currentUserId)

            result.onSuccess {
                _isNotificationEnabled.value = it
                Log.d(TAG, "fetchNotificationStatus: 알림 상태 fetch 성공")
            }.onFailure {
                Log.e(TAG, "fetchNotificationStatus: ${it.message}", )
            }
        }
    }
}
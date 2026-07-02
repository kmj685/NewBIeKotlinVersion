package com.newBie.new_bie.features.profile.presentation.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SettingViewModel: ViewModel() {
    val showDialog = MutableStateFlow<Boolean>(false)
}
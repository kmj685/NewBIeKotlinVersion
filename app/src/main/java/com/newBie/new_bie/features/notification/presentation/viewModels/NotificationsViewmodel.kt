package com.newBie.new_bie.features.notification.presentation.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class NotificationViewModel: ViewModel(){
    var isRefreshing = MutableStateFlow(false)
}
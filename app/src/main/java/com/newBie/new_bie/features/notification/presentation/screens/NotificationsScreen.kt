package com.newBie.new_bie.features.notification.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel

@Composable
fun NotificationsScreen(modifier: Modifier = Modifier, viewModel: NotificationViewModel) {

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Scaffold(
        topBar = { Column(modifier = Modifier.fillMaxWidth()){
            TopBarLayout("알림")
            Button(onClick = {}) {
                Text("모두 삭제")
            }
        } }
    ) {innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {},
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) { }
    }
}
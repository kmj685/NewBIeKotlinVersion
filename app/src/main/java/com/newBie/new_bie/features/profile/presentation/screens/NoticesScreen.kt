package com.newBie.new_bie.features.profile.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.core.utils.toKoreaLocalDateTime
import com.newBie.new_bie.core.utils.toTimeAgo
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.features.profile.presentation.components.SettingMenuItem
import com.newBie.new_bie.features.profile.presentation.viewModels.NoticesViewModel
import com.newBie.new_bie.ui.theme.GridColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoticesScreen(
    navController: NavController,
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    viewModel: NoticesViewModel = hiltViewModel()
){

    val isRead by notificationViewModel.isRead.collectAsState()

    val noticesList by viewModel.noticesList.collectAsState()

    Scaffold(
        topBar = {
            TopBarLayout(
                title = "공지사항",
                navController = navController,
                isRead = isRead
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GridColor)
                ) {
                    items(noticesList){
                        SettingMenuItem(
                            text = it.title,
                            createdAt = it.createdAt.toKoreaLocalDateTime().toTimeAgo(),
                            onClick = {
                                navController.navigate("${Routes.MY_PROFILE}/${Routes.SETTING}/${Routes.NOTICE}/${it.id}")
                            },
                        )
                    }
                }
            }
        }
    }
}
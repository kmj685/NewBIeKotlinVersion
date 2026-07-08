package com.newBie.new_bie.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.features.profile.presentation.viewModels.SettingViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.GridColor

@Composable
fun NotificationSettingScreen(
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    settingViewModel: SettingViewModel = hiltViewModel(),
    navController: NavController
){
    val isRead by notificationViewModel.isRead.collectAsState()
    val isEnabled by settingViewModel.isNotificationEnabled.collectAsState()
    Scaffold(
        topBar = {
            TopBarLayout(
                title = "알림 설정",
                isRead = isRead,
                navController = navController
            )
        },
        containerColor = Color.Transparent
    ) {innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(BlackColor)
                .padding(10.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GridColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "모든 알림",
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start
                    )
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = {
                            settingViewModel.setNotificationEnabled(it)
                        },
                    )
                }
            }
        }
    }
}
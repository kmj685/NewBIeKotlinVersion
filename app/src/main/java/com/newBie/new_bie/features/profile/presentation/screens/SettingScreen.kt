package com.newBie.new_bie.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.CommonDialog
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.features.profile.presentation.components.SettingMenuItem
import com.newBie.new_bie.features.profile.presentation.viewModels.SettingViewModel
import com.newBie.new_bie.ui.theme.GridColor
import com.newBie.new_bie.ui.theme.OrangeColor
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    navController: NavController,
    notificationViewModel: NotificationViewModel,
    viewModel: SettingViewModel = hiltViewModel()
){

    val scope = rememberCoroutineScope()
    val isRead by notificationViewModel.isRead.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()

    Scaffold(
        topBar = {
            TopBarLayout(
                title = "설정",
                navController = navController,
                isRead = isRead
            )
        },
        containerColor = Color.Transparent
    ) {innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(10.dp),
            contentAlignment = Alignment.TopCenter){
            Column(modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GridColor)
                ) {
                    val currentUserId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: ""

                    SettingMenuItem(
                        text = "공지사항",
                        icon = Icons.Default.Campaign,
                        onClick = {
                            navController.navigate("${Routes.MY_PROFILE}/${Routes.SETTING}/${Routes.NOTICE}")
                        }
                    )
                    SettingMenuItem(
                        text = "알림 설정",
                        icon = Icons.Default.Notifications,
                        onClick = {
                            if (currentUserId.isNotEmpty()){
                                navController.navigate("${Routes.MY_PROFILE}/${Routes.SETTING}/$currentUserId/${Routes.NOTIFICATION_SETTING}")
                            }
                        }
                    )

                    SettingMenuItem(
                        text = "차단 유저",
                        icon = Icons.Default.PersonOff,
                        onClick = {
                            if (currentUserId.isNotEmpty()){
                                navController.navigate("${Routes.MY_PROFILE}/${Routes.SETTING}/$currentUserId/${Routes.BLOCKED_USERS}")
                            }
                        }
                    )
                    SettingMenuItem(
                        text = "약관 개인정보 처리방침",
                        icon = Icons.Default.Description,
                        onClick = {
                            navController.navigate("${Routes.MY_PROFILE}/${Routes.SETTING}/${Routes.TERMS}")
                        }
                    )
                    SettingMenuItem(
                        text = "버전 정보 (1.4.0)",
                        onClick = {},
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                val masterId = "572addb9-303a-4118-84c7-894ca61cb557"
                val currentUserId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: ""

                if (currentUserId == masterId){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(GridColor)
                    ){
                        SettingMenuItem(
                            text = "관리자 메뉴",
                            icon = Icons.Default.AdminPanelSettings,
                            onClick = {  },
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                    scope.launch {
                        viewModel.showDialog.value = true
                    }},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeColor,
                        contentColor = Color.White
                    )) {
                    Text("로그아웃")
                }
            }
        }
        if (showDialog){
            CommonDialog(
                onConfirm = {
                    scope.launch {
                        SupabaseManager.logout()
                        viewModel.showDialog.value = false
                        navController.navigate(Routes.LOGIN)
                    }
                },
                onDismissRequest = {
                    viewModel.showDialog.value = false
                },
                askText = "정말 로그아웃 하시겠습니까?"
            )
        }
    }
}
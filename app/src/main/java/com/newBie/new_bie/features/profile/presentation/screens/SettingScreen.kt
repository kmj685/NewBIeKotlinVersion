package com.newBie.new_bie.features.profile.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.ui.theme.OrangeColor
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(navController: NavController, notificationViewModel: NotificationViewModel){

    val scope = rememberCoroutineScope()
    val isRead by notificationViewModel.isRead.collectAsState()

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
            .padding(horizontal = 10.dp),
            contentAlignment = Alignment.TopCenter){
            Column(modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally){
                Button(onClick = {
                    scope.launch {
                        SupabaseManager.logout()
                        navController.navigate(Routes.LOGIN)
                    }},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeColor,
                        contentColor = Color.White
                    )) {
                    Text("로그아웃")
                }
            }
        }
    }
}
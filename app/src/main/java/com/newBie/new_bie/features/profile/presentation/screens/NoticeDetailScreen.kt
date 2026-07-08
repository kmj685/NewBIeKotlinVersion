package com.newBie.new_bie.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.LinkifyText
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.core.utils.toFormattedDate
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.features.profile.presentation.viewModels.NoticeDetailViewModel
import com.newBie.new_bie.features.profile.presentation.viewModels.NoticesViewModel
import com.newBie.new_bie.ui.theme.AppTextStyle
import com.newBie.new_bie.ui.theme.GridColor

@Composable
fun NoticeDetailScreen(
    navController: NavController,
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    viewModel: NoticeDetailViewModel = hiltViewModel()
){
    val isRead by notificationViewModel.isRead.collectAsState()
    val notice by viewModel.notice.collectAsState()

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
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                notice?.title?.let {
                    Text(it, style = AppTextStyle.Title)
                }
                notice?.createdAt?.toFormattedDate()?.let {
                    Text(it, fontSize = 16.sp, textAlign = TextAlign.End, color = Color.Gray, modifier = Modifier.fillMaxWidth())
                }

                Spacer(modifier = Modifier.height(30.dp))

                notice?.content?.let {
                    SelectionContainer() {
                        LinkifyText(
                            text = it,
                            style = AppTextStyle.Content
                        )
                    }
                }
            }
        }
    }
}
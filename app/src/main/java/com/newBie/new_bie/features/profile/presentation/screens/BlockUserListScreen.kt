package com.newBie.new_bie.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.block.presentation.BlockUserViewModel
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.core.components.CommonDialog
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.post.presentation.components.SmallProfileComponent
import com.newBie.new_bie.ui.theme.OrangeColor


@Composable
fun BlockUserListScreen(
    viewModel: BlockUserViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel,
    navController: NavController,
){
    val isRead by notificationViewModel.isRead.collectAsState()
    val blockUserList by viewModel.blockedUserList.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val blockUser by viewModel.blockedUser.collectAsState()

    Scaffold(
        topBar = {
            TopBarLayout(
                title = "차단 유저",
                isRead = isRead,
                navController = navController,
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(BlackColor)
        ){
            if (blockUserList.isEmpty()){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BlackColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text("차단한 유저가 없습니다.", fontSize = 40.sp, color = Color.Gray, textAlign = TextAlign.Center)
                }

            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                items(
                    items = blockUserList,
                    key = { user -> user.id}
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 프로필은 가능한 넓게 (이미 내부에서 weight(1f)가 있음)
                        Box(modifier = Modifier.weight(1f)) {
                            SmallProfileComponent(
                                modifier = Modifier,
                                imageUrl = it.blockedUserId.profileImage,
                                nickName = it.blockedUserId.nickName,
                                introduce = it.blockedUserId.introduction,
                                userId = it.blockedUserId.id,
                                onImageClick = { navController.navigate("${Routes.MY_PROFILE}/${it.blockedUserId.id}")}
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.setDialogTargetUser(user = it)
                                viewModel.showDialog.value = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OrangeColor,
                                contentColor = Color.White,
                            )

                        ) {
                            Text("차단해제" , fontSize = 15.sp)
                        }
                    }
                }
            }
        }
        if (showDialog){
            CommonDialog(
                onConfirm = {
                    viewModel.cancelBlockUser(targetId = blockUser?.blockedUserId?.id ?: "")
                    viewModel.showDialog.value = false},
                onDismissRequest = {viewModel.showDialog.value = false},
                askText = "${blockUser?.blockedUserId?.nickName} 님을 차단 해제 하시겠습니까?"
            )
        }
    }
}
package com.newBie.new_bie.features.notification.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.notification.presentation.components.NotificationItems
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.features.post.presentation.components.SmallProfileComponent
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel,
    navController: NavController) {

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val notificationsList by viewModel.notificationsList.collectAsState()

    Scaffold(
        topBar = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)){
                Text("알림", color = OrangeColor, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)) {
                    IconButton(onClick = { viewModel.readAllNotification()},
                        colors = IconButtonDefaults.iconButtonColors(containerColor = OrangeColor)) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "알림 모두 읽음",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { viewModel.deletedAllNotification() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Red)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "알림 모두 삭제",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.getNotificationsList() },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = BlackColor),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (notificationsList.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(), // 부모의 사이즈 만큼
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "새로운 알림이 없습니다.",
                                color = OrangeColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else itemsIndexed(
                    items = notificationsList,
                    key = { _, item -> item.id} // _: index는 안쓸거니까 무시해, item.id로 하나하나 구별할거야
                ) { _, item ->
                    // 스와이프 상태 기억 및 로직 트리거
                    val dismissState = rememberSwipeToDismissBoxState(
                        initialValue = SwipeToDismissBoxValue.Settled // 평소 상태는 정지(Settled 상태로)
                    )

                    // 상태 변화(currentValue)를 감지하여 로직 처리
                    LaunchedEffect(dismissState.currentValue) {
                        when(dismissState.currentValue){
                            // 왼쪽에서 오른쪽으로 스와이프 했을 때: 개별 읽음 함수
                            SwipeToDismissBoxValue.StartToEnd -> {
                                viewModel.readNotification(item.id)
                                // 읽음 처리 후 아이템이 사라지지 않고 제자리로 슥 돌아오게 만듦
                                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                            }
                            // 오른쪽에서 왼쪽으로 스와이프 했을 때: 개별 삭제 함수
                            SwipeToDismissBoxValue.EndToStart -> {
                                viewModel.deletedNotification(item.id)
                            }
                            SwipeToDismissBoxValue.Settled -> {
                            // 가만히 제자리에 있을 때는 아무것도 하지 않음
                            }
                        }
                    }

                    // 최종 UI
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = { // 해석: 본래 내용 뒤의 내용물 설정
                            val alignment =
                                when (dismissState.dismissDirection) {
                                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                                SwipeToDismissBoxValue.Settled -> Alignment.CenterStart
                                }
                            val iconTintColor =
                                when (dismissState.dismissDirection){
                                SwipeToDismissBoxValue.StartToEnd -> OrangeColor
                                SwipeToDismissBoxValue.EndToStart -> Color.Red
                                SwipeToDismissBoxValue.Settled -> Color.Transparent
                                }
                            val icon =
                                when (dismissState.dismissDirection){
                                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Check
                                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                                SwipeToDismissBoxValue.Settled -> null
                                }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Transparent)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = alignment
                            ) {
                                if (icon != null) {
                                    IconButton(onClick = {},
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = iconTintColor)) {
                                        Icon(
                                            imageVector = icon, contentDescription = null, tint = Color.White
                                        )
                                    }
                                }
                            }
                        },
                        content = {
                            NotificationItems(
                                modifier = Modifier,
                                imageUrl = item.senderId.profileImage,
                                followingPostingUserName = item.senderId.nickName,
                                followerUserName = item.senderId.nickName,
                                guestbooksSenderUserName = item.senderId.nickName,
                                type = item.type,
                                isRead = item.isRead,
                                onClick = {
                                    viewModel.readNotification(item.id)

                                    when(item.type){
                                        "NEW_POST" -> navController.navigate("${Routes.POST}/${item.targetId.toIntOrNull()}")
                                        "NEW_FOLLOWER" -> navController.navigate("${Routes.MY_PROFILE}/${item.targetId}")
                                        "NEW_GUESTBOOK" -> navController.navigate("${Routes.GUESTBOOKS}/${item.targetId.toIntOrNull()}")
                                    }
                                },
                            )
                        }
                    )
                }
            }
        }
    }
}
package com.newBie.new_bie.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.BottomTapBar
import com.newBie.new_bie.core.components.TopBarTitleText
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.PageSet
import com.newBie.new_bie.features.post.presentation.components.SmallProfileComponent
import com.newBie.new_bie.features.profile.presentation.viewModels.MyProfileViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor
import io.github.jan.supabase.auth.auth

@Composable
fun MyProfileScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: MyProfileViewModel = viewModel<MyProfileViewModel>()){

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = {TopBarTitleText("마이페이지")},
        bottomBar = {BottomTapBar(navController = navController, pageSet = PageSet.PROFILE)},
        containerColor = Color.Transparent
    ) { innerPadding ->

        Box(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(BlackColor)){

            // 항상 맨 위에 있어야하는 최상단 UI (TopBarTitleText, BottomTapBar)
            Column(modifier = Modifier
                .fillMaxSize()) {
                // 본문 시작 -> 여기에 본격적으로 내용 들어가면 됨
                // 당겨서 새로고침
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { viewModel.refreshAll() },
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Column() {
                        // SmallProfileComponent에 따로 null처리 해놨기 때문에 안해도 됨
                        SmallProfileComponent(
                            modifier = Modifier,
                            imageUrl = user?.profileImage,
                            nickName = user?.nickName,
                            introduce = user?.introduction,
                            userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id,
                            onImageClick = {} //TODO: 누르면 프로필 편집되게 PhotoPicker 사용
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 게시물 수, 팔로워, 팔로잉
                        Row(modifier = Modifier,
                            horizontalArrangement = Arrangement.Center) {
                            // TODO: onClick 부분에 팔로워,팔로잉 액티비티로 navigation 연결 탭으로 해서 인덱스 = 0로 연결
                            Column(modifier = Modifier.clickable(onClick = {})) {
                                Text("${user?.followerCount}", color = OrangeColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("팔로워", color = Color.White)
                            }
                            // TODO: onClick 부분에 팔로워,팔로잉 액티비티로 navigation 연결 탭으로 인덱스 = 1로 연결
                            Column(modifier = Modifier.clickable(onClick = {})) {
                                Text("${user?.followerCount}", color = OrangeColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("팔로잉", color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // TODO: SetProfileScreen으로 이동
                        Button(modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OrangeColor),
                            onClick = {}) {
                            Text("프로필 수정", color = Color.White)
                        }
                    }
                }

            }

        }

    }


}
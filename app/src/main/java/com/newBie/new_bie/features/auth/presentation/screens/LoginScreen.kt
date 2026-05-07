package com.newBie.new_bie.features.auth.presentation.screens

import BouncingBallsBackground
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.R
import com.newBie.new_bie.core.utils.findActivity
import com.newBie.new_bie.features.auth.presentation.viewModels.AuthViewModel
import androidx.compose.ui.geometry.Rect

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: AuthViewModel = viewModel<AuthViewModel>()) {
    val context = LocalContext.current
    val activity = context.findActivity()

    // 장애물(UI 영역) 좌표를 저장할 리스트
    // 여러 개의 장애물 영역을 저장할 맵 (ID를 키로 사용해서 업데이트)
    var obstacleMap by remember { mutableStateOf(mapOf<String, Rect>()) }
    // 맵이 바뀔 때마다 리스트를 갱신
    val obstacleRects = remember(obstacleMap) { obstacleMap.values.toList() }

    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {

        // 1. 배경 애니메이션 (장애물 정보 전달)
        BouncingBallsBackground(obstacles = obstacleRects)

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(80.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.new_bie_logo),
                contentDescription = "뉴비 로고",
                modifier = Modifier
                    .width(300.dp)
                    .height(70.dp)
                    .onGloballyPositioned { coords ->
                        obstacleMap = obstacleMap + ("logo" to coords.boundsInWindow())
                    }
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 30.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.signInWithGoogle(activity) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .onGloballyPositioned { coords ->
                            obstacleMap = obstacleMap + ("google" to coords.boundsInWindow())
                        },
                    // 중요: 버튼의 기본 여백을 0으로 만들어야 경계선이 정확해집니다.
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = "구글 로고",
                            modifier = Modifier.size(30.dp)
                        )
                        Text("구글 로그인", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }

                }
                Button(onClick = {viewModel.signInWithGithub()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .onGloballyPositioned { coords ->
                            obstacleMap = obstacleMap + ("github" to coords.boundsInWindow())
                        },
                    // 중요: 버튼의 기본 여백을 0으로 만들어야 경계선이 정확해집니다.
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.github_logo),
                            contentDescription = "구글 로고",
                            modifier = Modifier.size(25.dp)
                        )
                        Text("GitHub 로그인", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
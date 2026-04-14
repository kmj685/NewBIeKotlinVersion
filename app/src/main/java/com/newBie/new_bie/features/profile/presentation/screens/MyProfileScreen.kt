package com.newBie.new_bie.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.BottomTapBar
import com.newBie.new_bie.core.components.TopBarTitleText
import com.newBie.new_bie.core.utils.PageSet
import com.newBie.new_bie.features.profile.presentation.viewModels.MyProfileViewModel
import com.newBie.new_bie.ui.theme.BlackColor

@Composable
fun MyProfileScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: MyProfileViewModel = viewModel<MyProfileViewModel>()){
    Box(modifier = modifier
        .fillMaxSize()
        .background(BlackColor)){

        // 항상 맨 위에 있어야하는 최상단 UI (TopBarTitleText, BottomTapBar)
        Column(modifier = Modifier
            .fillMaxSize()) {
            TopBarTitleText("마이페이지")

            // 본문 시작 -> 여기에 본격적으로 내용 들어가면 됨
            Column(modifier = Modifier
                .weight(1f)
                .padding(10.dp)) {

                // 당겨서 새로고침
//                PullToRefreshBox(
//                    isRefreshing = TODO(),
//                    onRefresh = TODO(),
//                    modifier = TODO(),
//                    state = TODO(),
//                    contentAlignment = TODO(),
//                    indicator = TODO()
//                ) { }
            }

            BottomTapBar(
                navController = navController,
                pageSet = PageSet.PROFILE
            )
        }
    }
}
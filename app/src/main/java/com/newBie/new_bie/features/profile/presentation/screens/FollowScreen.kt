package com.newBie.new_bie.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.features.profile.presentation.components.FollowItem
import com.newBie.new_bie.features.profile.presentation.viewModels.FollowViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor
import kotlinx.coroutines.launch

@Composable
fun FollowScreen(modifier: Modifier = Modifier, navController: NavController, targetUserId: String? = null, initialTab: Int?, viewModel: FollowViewModel = viewModel<FollowViewModel>()){

    val pagerTitle: List<String> = listOf("팔로워", "팔로잉")
    val pagerState = rememberPagerState(pageCount = {pagerTitle.size})
    val scope = rememberCoroutineScope()
    val user by viewModel.user.collectAsState()
    val followerList by viewModel.followerList.collectAsState()
    val followingList by viewModel.followingList.collectAsState()
    val isFollowing by viewModel.isFollowing.collectAsState()


    LaunchedEffect(targetUserId) {
        viewModel.loadData(targetUserId)
    }

    LaunchedEffect(initialTab) {
        if (initialTab != null){
            pagerState.scrollToPage(initialTab)
        }
    }

    Scaffold(
        topBar = {
            TopBarLayout("${user?.nickName}", navController = navController)},
        containerColor = Color.Transparent) {innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(BlackColor)){
            Column(modifier = Modifier
                .fillMaxWidth()){
                SecondaryTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = Color.Transparent,
                    contentColor = OrangeColor,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                            color = OrangeColor
                        )
                    }
                ) {
                    pagerTitle.forEachIndexed { index, string ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            unselectedContentColor = Color.White,
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            },
                            text = {
                                Text(string)
                            }
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) { page ->
                    // 각 페이지(팔로워/팔로잉)에 맞는 리스트 선택
                    val pagerList = if (page == 0) followerList else followingList

                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(10.dp)
                    ) {
                        // 리스트 데이터를 돌면서 FollowItem을 생성
                        items(pagerList) { followData ->
                            viewModel.myId?.let { myId->
                                FollowItem(
                                    followData = followData,
                                    onButtonClick = {
                                        followData.users.id.let {targetUserId ->
                                            viewModel.toggleFollow(targetUserId)
                                        }
                                    },
                                    navController = navController,
                                    isFollowing = followData.isFollowing,
                                    myId = myId
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
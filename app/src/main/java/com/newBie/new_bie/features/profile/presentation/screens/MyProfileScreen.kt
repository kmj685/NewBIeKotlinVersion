package com.newBie.new_bie.features.profile.presentation.screens

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.BottomTapBar
import com.newBie.new_bie.core.components.TopBarTitleText
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.PageSet
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.post.presentation.components.SmallProfileComponent
import com.newBie.new_bie.features.post.presentation.screens.HomeScreen
import com.newBie.new_bie.features.profile.presentation.viewModels.MyProfileViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun MyProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyProfileViewModel = viewModel<MyProfileViewModel>()
){
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val user by viewModel.user.collectAsState()
    val post by viewModel.posts.collectAsState()

    // 페이저 상태는 그대로 유지합니다.
    val pagerTitle : List<ImageVector> = listOf(Icons.Default.GridOn, Icons.Default.Edit, Icons.Default.Diversity3)
    val pagerState = rememberPagerState(pageCount = { pagerTitle.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBarTitleText("마이페이지") },
        bottomBar = { BottomTapBar(navController = navController, pageSet = PageSet.PROFILE) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                if(!isRefreshing){
                    viewModel.isRefreshing.value = true
                    viewModel.refreshAll()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(BlackColor)
                .padding(10.dp)
        ){

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                // 프로필 상단 영역
                item {
                    Column() {
                        SmallProfileComponent(
                            modifier = Modifier,
                            imageUrl = user?.profileImage,
                            nickName = user?.nickName,
                            introduce = user?.introduction,
                            userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id,
                            onImageClick = {}
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${user?.postCount}", color = OrangeColor, fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.Center)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("게시글", color = Color.White)
                            }
                            Column(modifier = Modifier.clickable(onClick = {}),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${user?.followerCount}", color = OrangeColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("팔로워", color = Color.White)
                            }
                            Column(modifier = Modifier.clickable(onClick = {}),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${user?.followerCount}", color = OrangeColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("팔로잉", color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            onClick = {}) {
                            Text("프로필 수정", color = Color.Black)
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                // 탭바 영역 (item을 새로 만들어서 분리)
                item {
                    SecondaryTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = Color.Transparent,
                        contentColor = OrangeColor,
                        indicator = { TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                            color = OrangeColor
                        )},
                        divider = {}
                    ) {
                        pagerTitle.forEachIndexed { index, iconVector ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                unselectedContentColor = Color.White,
                                onClick = {
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                },
                                icon = { Icon(imageVector = iconVector, contentDescription = null) }
                            )
                        }
                    }
                }

                // 그리드 영역 (가장 중요한 부분: Column 내부가 아니라 LazyColumn의 직접 자식으로 배치)
                item {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(), // 부모 LazyColumn의 높이만큼 차지하게 함
                        verticalAlignment = Alignment.Top,
                        userScrollEnabled = true

                    ) { page ->
                        when(page){
                            0 -> UserFeedGridScreen(
                                posts = post,
                                onPostClick = { navController.navigate("${Routes.POST}/${it}") },
                                onLoadMore = { viewModel.fetchMorePosts() }
                            )
                        }
                    }
                }
            }
        }
    }
}
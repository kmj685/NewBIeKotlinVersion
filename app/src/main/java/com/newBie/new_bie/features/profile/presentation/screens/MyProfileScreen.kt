package com.newBie.new_bie.features.profile.presentation.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.BaseAsyncImage
import com.newBie.new_bie.core.components.BottomTapBar
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.PageSet
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.features.post.presentation.components.SmallProfileComponent
import com.newBie.new_bie.features.profile.presentation.components.AddGuestbookBottomSheet
import com.newBie.new_bie.features.profile.presentation.components.UserFeedGridItem
import com.newBie.new_bie.features.profile.presentation.components.UserGuestbookListItem
import com.newBie.new_bie.features.profile.presentation.viewModels.AddGuestbooksBottomSheetViewModel
import com.newBie.new_bie.features.profile.presentation.viewModels.MyProfileViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    targetUserId: String? = null,
    viewModel: MyProfileViewModel = hiltViewModel(),
    addGuestbooksBottomSheetViewModel: AddGuestbooksBottomSheetViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel
){
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val user by viewModel.user.collectAsState()
    val post by viewModel.posts.collectAsState()

    // 페이저 상태는 그대로 유지합니다.
    val pagerTitle : List<ImageVector> = listOf(Icons.Default.GridOn, Icons.Default.Edit, Icons.AutoMirrored.Filled.MenuBook)
    val pagerState = rememberPagerState(pageCount = { pagerTitle.size })
    val scope = rememberCoroutineScope()

    // 프로필 사진 확대 기능
    var isExpanded by remember { mutableStateOf(false) }
    val zoomState = rememberZoomState()

    val myId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
    val myProfile = myId == targetUserId || targetUserId == null

    val isFollowing by viewModel.isFollowing.collectAsState()

    val focusManager = LocalFocusManager.current

    val isRead by notificationViewModel.isRead.collectAsState()

    val guestbooks by viewModel.guestbooks.collectAsState()

    // 방명록 작성 BottomModalSheet 플래그 값
    var bottomModalSheetFlag by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // 게시물 수정하면 자동으로 refresh
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val needRefresh by savedStateHandle?.getStateFlow("need_refresh", false)?.collectAsState()
        ?:remember { mutableStateOf(false) }

    // 등록에 성공했다면 ModalBottomSheet 내리기
    LaunchedEffect(Unit) {
        addGuestbooksBottomSheetViewModel.guestbookSaveSuccess.collect { isSuccess ->
            if(isSuccess) {
                bottomModalSheetFlag = false
                viewModel.refreshAll()
            }
        }
    }
    LaunchedEffect(needRefresh) {
        if (needRefresh){
            viewModel.refreshAll()
            savedStateHandle?.set("need_refresh", false)
        }
    }

    Scaffold(
        topBar = {
            TopBarLayout(
                title = if (myProfile) {
                    "마이페이지"
                } else {
                    user?.nickName ?: ""
                },
                moreVert = true,
                targetId = targetUserId,
                focusManager = focusManager,
                setting = true,
                navController = navController,
                isRead = isRead
            )},
        bottomBar = { BottomTapBar(navController = navController, pageSet = PageSet.PROFILE) },
        containerColor = Color.Transparent
    ) {innerPadding ->
        SharedTransitionLayout(
            modifier = Modifier.padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = isExpanded,
            ) {targetExpended ->
                if (!targetExpended){
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                if(!isRefreshing){
                                    viewModel.isRefreshing.value = true
                                    viewModel.refreshAll()
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .background(BlackColor)
                                .padding(horizontal = 10.dp)
                        ){

                            LazyColumn(modifier = Modifier.fillMaxSize()) {

                                // 프로필 상단 영역
                                item {
                                    Column() {
                                        SmallProfileComponent(
                                            modifier = Modifier.sharedElement(
                                                rememberSharedContentState(key = "image_${user?.profileImage}"),
                                                animatedVisibilityScope = this@AnimatedContent),
                                            imageUrl = user?.profileImage,
                                            nickName = user?.nickName,
                                            introduce = user?.introduction,
                                            userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id,
                                            onImageClick = { isExpanded = true }
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
                                            Column(modifier = Modifier.clickable(onClick = {
                                                val userId = user?.id
                                                navController.navigate("${Routes.MY_PROFILE}/$userId/${Routes.FOLLOW}?initialTab=0")
                                            }),
                                                horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("${user?.followerCount}", color = OrangeColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text("팔로워", color = Color.White)
                                            }
                                            Column(modifier = Modifier.clickable(onClick = {
                                                val userId = user?.id
                                                navController.navigate("${Routes.MY_PROFILE}/$userId/${Routes.FOLLOW}?initialTab=1")
                                            }),
                                                horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("${user?.followingCount}", color = OrangeColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text("팔로잉", color = Color.White)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(20.dp))

                                        if (myProfile){
                                            Button(modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                                onClick = {
                                                    navController.navigate("${Routes.MY_PROFILE}/${Routes.UPDATE_PROFILE}")
                                                }) {
                                                Text("프로필 수정", color = Color.White)
                                            }
                                        } else {
                                            Button(modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = if (isFollowing) Color.Gray else OrangeColor),
                                                onClick = {
                                                    user?.id?.let {
                                                        viewModel.toggleFollow(it)
                                                    }
                                                }) {
                                                Text(if(isFollowing) "팔로잉" else "팔로우", color = Color.White)
                                            }
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
                                            0 -> UserFeedGridItem(
                                                posts = post,
                                                onPostClick = { navController.navigate("${Routes.POST}/${it}") },
                                                onLoadMore = { viewModel.fetchMorePosts() }
                                            )
                                            1 -> Box(
                                                modifier = Modifier
                                                    .fillParentMaxSize(),
                                                contentAlignment = Alignment.TopCenter) {
                                                Text("일지 coming soon...", color = Color.Gray, fontSize = 30.sp,
                                                    modifier = Modifier.padding(top = 200.dp))
                                            }
                                            2 -> Column(
                                                modifier = Modifier
                                                    .fillParentMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally) {
                                                if(!myProfile){
                                                    Button(
                                                        onClick = {
                                                            bottomModalSheetFlag = true
                                                        },
                                                        shape = RoundedCornerShape(12.dp),
                                                        modifier = Modifier.fillMaxWidth(),
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = OrangeColor,
                                                            contentColor = Color.White
                                                        )
                                                    ) {
                                                        Text("방명록 작성", fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                                UserGuestbookListItem(
                                                    onLoadMore = { viewModel.fetchMoreGuestbooks() },
                                                    guestbooks = guestbooks,
                                                    onClick = { navController.navigate("${Routes.GUESTBOOKS}/${it}")}
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            if (bottomModalSheetFlag) {
                                AddGuestbookBottomSheet(
                                    viewModel= addGuestbooksBottomSheetViewModel,
                                    screenHeight = screenHeight,
                                    sheetState = sheetState,
                                    onDismiss = {bottomModalSheetFlag = false}
                                )
                            }
                        }
                    } else{
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.7f)),
                            contentAlignment = Alignment.Center){
                            BaseAsyncImage(
                                model = user?.profileImage,
                                contentDescription = "프로필 확대 이미지",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(250.dp)
                                    .sharedElement(
                                        rememberSharedContentState(key = "image_${user?.profileImage}"),
                                        animatedVisibilityScope = this@AnimatedContent
                                    )
                                    .clip(CircleShape)
                                    .zoomable(zoomState)
                            )
                            // 닫기 버튼
                            IconButton(
                                onClick = { isExpanded = false },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "닫기", tint = Color.White)
                            }
                        }
                    // 안드로이드 뒤로가기 키 설정
                    BackHandler() { isExpanded = false }
                }
            }
        }
    }
}
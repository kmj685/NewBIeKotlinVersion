package com.newBie.new_bie.features.post.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.post.presentation.components.PostItem
import com.newBie.new_bie.features.post.presentation.components.SmallProfileComponent
import com.newBie.new_bie.features.post.presentation.viewModels.SearchResultViewModel
import com.newBie.new_bie.ui.theme.OrangeColor
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(modifier: Modifier = Modifier, navController: NavController,viewModel: SearchResultViewModel = viewModel<SearchResultViewModel>(), initialQuery: String = "", ) {

    val posts by viewModel.posts.collectAsState()
    val users by viewModel.users.collectAsState()
    val keyword by viewModel.keyword.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    // Flutter의 TabController를 대체합니다.
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    // 🔥 [추가] 처음 진입 시 검색어가 있다면 검색 실행
    LaunchedEffect(Unit) {
        if (initialQuery.isNotBlank()) {
            viewModel.search(initialQuery)
        }
    }

    // ViewModel의 selectedTab과 PagerState 동기화
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onTabSelected(pagerState.currentPage)
    }

    LaunchedEffect(selectedTab) {
        if (pagerState.currentPage != selectedTab) {
            pagerState.animateScrollToPage(selectedTab)
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                // 상단 검색바
                OutlinedTextField(
                    value = keyword,
                    onValueChange = { viewModel.keyword.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("검색어를 입력하세요.") },
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.search(keyword) }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { viewModel.search(keyword) }),
                    singleLine = true
                )

                // TabBar (전체, 게시글, 사용자)
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = OrangeColor, // 테마에 정의된 색상 사용
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = OrangeColor
                        )
                    }
                ) {
                    val tabs = listOf("전체", "게시글", "사용자")
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            },
                            text = { Text(title) },
                            selectedContentColor = OrangeColor,
                            unselectedContentColor = Color.Gray
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            when (pageIndex) {
                0 -> SearchAllView(viewModel, navController)
                1 -> SearchPostView(viewModel, navController)
                2 -> SearchUserView(viewModel, navController)
            }
        }
    }
}

// --- 하위 뷰 컴포저블 ---

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchAllView(viewModel: SearchResultViewModel, navController: NavController) {
    val posts by viewModel.posts.collectAsState()
    val users by viewModel.users.collectAsState()

    if (posts.isEmpty() && users.isEmpty()) {
        EmptyResultView()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            // 사용자 요약 섹션
            if (users.isNotEmpty()) {
                item { Text("유저 : ${users.size}명", fontWeight = FontWeight.Bold) }
                items(users) { user -> // 전체보기에서는 일부만 표시 (Flutter 로직 대응)
                    SmallProfileComponent(
                        modifier = Modifier.fillMaxWidth(),
                        imageUrl = user.profileImage, // 엔티티 필드명에 맞게 조정
                        nickName = user.nickName ?: "",
                        introduce = user.introduction,
                        userId = user.id,
                        onImageClick = {
                            navController.navigate("user_profile/${user.id}")
                        }
                    )
                }
                item {
                    Button(
                        onClick = { viewModel.onTabSelected(2) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangeColor)
                    ) {
                        Text("유저 검색결과 더보기", color = Color.White)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // 게시물 요약 섹션
            if (posts.isNotEmpty()) {
                item { Text("게시물 : ${posts.size}개", fontWeight = FontWeight.Bold) }
                items(posts.take(5)) { post ->
                    val index = posts.indexOf(post)
                    PostItem(
                        post = post,
                        onLike = { viewModel.likeToggle(index, post.id) },
                        onDelete = { viewModel.deletePost(post.id) },
                        onClick = {navController.navigate("${Routes.POST}/${it}")}
                    )
                }
                item {
                    Button(
                        onClick = { viewModel.onTabSelected(1) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("게시물 검색결과 더보기")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchPostView(viewModel: SearchResultViewModel, navController: NavController) {
    val posts by viewModel.posts.collectAsState()
    val listState = rememberLazyListState()

    // 페이징 감지
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (index != null && index >= posts.size - 2) {
                    viewModel.fetchMorePosts()
                }
            }
    }

    if (posts.isEmpty()) {
        EmptyResultView()
    } else {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
            itemsIndexed(posts) { index, post ->
                PostItem(
                    post = post,
                    onLike = { viewModel.likeToggle(index, post.id) },
                    onDelete = { viewModel.deletePost(post.id) },
                    onClick = {navController.navigate("${Routes.POST}/${it}")}
                )
            }
        }
    }
}

@Composable
fun SearchUserView(viewModel: SearchResultViewModel, navController: NavController) {
    val users by viewModel.users.collectAsState()
    val listState = rememberLazyListState()

    // 페이징 감지
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (index != null && index >= users.size - 2) {
                    viewModel.fetchMoreUsers()
                }
            }
    }

    if (users.isEmpty()) {
        EmptyResultView()
    } else {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
            items(users) { user ->
                SmallProfileComponent(
                    modifier = Modifier.fillMaxWidth(),
                    imageUrl = user.profileImage,
                    nickName = user.nickName ?: "",
                    introduce = user.introduction,
                    userId = user.id,
                    onImageClick = {
                        navController.navigate("user_profile/${user.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyResultView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("검색 결과가 없습니다.", fontSize = 20.sp, color = Color.Gray)
    }
}
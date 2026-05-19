package com.newBie.new_bie.features.post.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cloudinary.transformation.BackgroundColor
import com.newBie.new_bie.core.components.BottomTapBar
import com.newBie.new_bie.core.utils.OrderByType
import com.newBie.new_bie.core.utils.PageSet
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.core.utils.toKoreaLocalDateTime
import com.newBie.new_bie.core.utils.toTimeAgo
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.presentation.components.PostItem
import com.newBie.new_bie.features.post.presentation.components.buttons.CategoryButton
import com.newBie.new_bie.features.post.presentation.components.buttons.PostEditCategoryBtn
import com.newBie.new_bie.features.post.presentation.components.likesAndComments.CommentItem
import com.newBie.new_bie.features.post.presentation.viewModels.HomeViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor
import kotlin.collections.emptyList
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import com.newBie.new_bie.R
import com.newBie.new_bie.features.post.presentation.components.likesAndComments.CommentBottomSheet
import com.newBie.new_bie.ui.theme.GridColor
import io.ktor.util.collections.setValue
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController, viewModel : HomeViewModel = viewModel<HomeViewModel>()) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectPostId by viewModel.selectPostId.collectAsState()
    val commentsList by viewModel.comments.collectAsState()

    var userInput by remember { mutableStateOf("") }
    val userCommentInput by viewModel.userCommentInput.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    val categoryList by viewModel.categoryList.collectAsState()
    val selectCategory by viewModel.selectCategory.collectAsState()
    val postItemList by viewModel.posts.collectAsState()
    val orderType by viewModel.type.collectAsState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val focusManager = LocalFocusManager.current



    LaunchedEffect(listState) {
        launch {
            snapshotFlow { listState.layoutInfo }
                .collect { layoutInfo ->

                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItemIndex =
                    layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                if (lastVisibleItemIndex >= totalItems - 3) {
                    viewModel.fetchNextPosts()
                }
            }
        }
        // 포커스 해제
        launch {
            snapshotFlow { listState.isScrollInProgress }
                .distinctUntilChanged() //스크롤이 시작 될 때 딱 한 번만 작동
                .collect { isScrolling ->
                    if (isScrolling){
                        focusManager.clearFocus()
                    }
                }
        }
    }

//    LaunchedEffect(listState.isScrollInProgress) {
//        if (listState.isScrollInProgress){
//            focusManager.clearFocus()
//        }
//    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.new_bie_logo),
                    contentDescription = "홈 로고",
                    modifier = Modifier
                        .padding(10.dp)
                        .height(30.dp)
                )
                IconButton(onClick = { navController.navigate(Routes.NOTIFICATION) }) {
                    Icon(Icons.Default.Notifications, contentDescription = "알림", tint = OrangeColor)
                }
            }
                 },
        bottomBar = {BottomTapBar(navController, PageSet.HOME)},
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()
            .pointerInput(Unit){
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }){
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = OrangeColor,
                            unfocusedBorderColor = BlackColor

                            ),
                        shape = RoundedCornerShape(12.dp),
                        value = userInput,
                        onValueChange = { userInput = it },
                        placeholder = { Text("검색어를 입력하세요") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (userInput.isNotBlank()) {
                                    // 검색 화면으로 이동 (작성하신 NavHost 경로 기준)
                                    navController.navigate("${Routes.HOME}/${Routes.SEARCH}?query=$userInput")
                                }
                            }
                        ),
//            colors = OutlinedTextFieldDefaults.colors(),
                        trailingIcon = {
                            Icon(
                                Icons.Default.Search,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable(onClick = {
                                        if (userInput.isNotBlank()) {
                                            // 2. 돋보기 아이콘 클릭 시 이동
                                            navController.navigate("${Routes.HOME}/${Routes.SEARCH}?query=$userInput")
                                        }
                                    }),
                                contentDescription = null,
                                tint = BlackColor
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow() {
                        items(categoryList) { category ->
                            CategoryButton(
                                category,
                                selectCategory,
                                { viewModel.changeCategory(category) })
                        }
                    }
//                    Spacer(modifier = Modifier.height(8.dp))

                    // 🔽 정렬 Dropdown
                    Box(modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(50.dp)
                        .align(Alignment.End),
                        contentAlignment = Alignment.CenterEnd) {

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {

                            OutlinedTextField(
                                readOnly = true,
                                value = when (orderType) {
                                    OrderByType.NEW_FIRST -> "최신 순"
                                    OrderByType.OLD_FIRST -> "오래된 순"
                                    OrderByType.LIKES_FIRST -> "좋아요 순"
                                },
                                onValueChange = {},
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier.menuAnchor(),
                                textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTrailingIconColor = OrangeColor,
                                    unfocusedTrailingIconColor = OrangeColor)
                            )

                            ExposedDropdownMenu(
                                modifier = Modifier.background(color = GridColor),
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {

                                DropdownMenuItem(
                                    text = { Text("최신 순", color = OrangeColor) },
                                    onClick = {
                                        viewModel.changeOrder(OrderByType.NEW_FIRST)
                                        expanded = false
                                    },

                                    )

                                DropdownMenuItem(
                                    text = { Text("오래된 순", color = OrangeColor) },
                                    onClick = {
                                        viewModel.changeOrder(OrderByType.OLD_FIRST)
                                        expanded = false
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("좋아요 순", color = OrangeColor) },
                                    onClick = {
                                        viewModel.changeOrder(OrderByType.LIKES_FIRST)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = { viewModel.refresh() },
                        modifier = Modifier.weight(1f)
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(20.dp)

                        ) {
                            itemsIndexed(postItemList) { index, post ->
                                PostItem(
                                    post = post,
                                    onLike = { viewModel.likeToggle(index, post.id) },
                                    onDelete = { viewModel.deletePost(post.id) },
                                    onClick = { navController.navigate("${Routes.POST}/${it}") },
                                    onComments = { viewModel.fetchComments(post.id) },
                                    navController = navController
                                )
                            }

                        }
                    }


                }
                if (selectPostId != null) {
                    CommentBottomSheet(
                        viewModel = viewModel,
                        screenHeight = screenHeight,
                        sheetState = sheetState,
                        onDismiss = { }
                    )
                }
            }
        }
    }
}
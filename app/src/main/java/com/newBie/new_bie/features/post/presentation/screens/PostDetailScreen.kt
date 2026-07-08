package com.newBie.new_bie.features.post.presentation.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.newBie.new_bie.core.components.BaseAsyncImage
import com.newBie.new_bie.core.components.LinkifyText
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.BottomSheetType
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.core.utils.toKoreaLocalDateTime
import com.newBie.new_bie.core.utils.toTimeAgo
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.features.post.domain.entities.PostImageEntity
import com.newBie.new_bie.features.post.domain.entities.PostUserEntity
import com.newBie.new_bie.features.post.presentation.components.SmallProfileComponent
import com.newBie.new_bie.features.post.presentation.components.buttons.PostMoreVertButton
import com.newBie.new_bie.features.post.presentation.components.likesAndComments.CommentBottomSheet
import com.newBie.new_bie.features.post.presentation.components.likesAndComments.LikeUserListBottomSheet
import com.newBie.new_bie.features.post.presentation.interfaces.CommentBottomSheetViewModel
import com.newBie.new_bie.features.post.presentation.viewModels.HomeViewModel
import com.newBie.new_bie.features.post.presentation.viewModels.PostDetailViewModel
import com.newBie.new_bie.ui.theme.AppTextStyle
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import com.tbuonomo.viewpagerdotsindicator.compose.type.WormIndicatorType
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel : PostDetailViewModel = viewModel<PostDetailViewModel>(),
    notificationViewModel: NotificationViewModel,
    id: Int) {

    val bottomSheetType by viewModel.bottomSheetType.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    val selectPostId by viewModel.selectPostId.collectAsState()
    val post by viewModel.post.collectAsState()
    val images by viewModel.images.collectAsState()
    val user: PostUserEntity? = post?.user
    val currentUserId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: ""

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // 꽉 차는 값 flag 값
    var isExpanded by remember { mutableStateOf(false) }
    // 사진 한장 한장가져올 값
    var selectedImage by remember { mutableStateOf<PostImageEntity?>(null) }

    val focusManager = LocalFocusManager.current
    val isRead by notificationViewModel.isRead.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val isDeletedPost by viewModel.isDeletedPost.collectAsState()

    LaunchedEffect(Unit) {
        if (id != 0) {
            viewModel.fetchPost(id)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBarLayout(
                title = "게시물",
                focusManager = focusManager,
                navController = navController,
                isRead = isRead
            )
        },
    ) { innerPadding ->
        if (isDeletedPost) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlackColor),
                contentAlignment = Alignment.Center
            ){
                Text(text = "삭제된 게시글 입니다.", fontSize = 40.sp, color = Color.Gray)
            }
        } else {
            SharedTransitionLayout(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
            ){
                AnimatedContent(
                    targetState = isExpanded,
                    label = "ImageTransition"
                ) { targetExpended ->
                    if (!targetExpended){
                        Column(
                            modifier = modifier.fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 10.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    SmallProfileComponent(
                                        modifier = Modifier.weight(1f),
                                        imageUrl = user?.profileImage ?: "",
                                        nickName = user?.nickName ?: "",
                                        introduce = post?.createdAt?.toKoreaLocalDateTime()?.toTimeAgo(),
                                        userId = user?.id ?: "",
                                        {
                                            navController.navigate("${Routes.MY_PROFILE}/${user?.id}")
                                        }
                                    )
                                    PostMoreVertButton(
                                        targetId = user?.id ?: "",
                                        targetIdNickname = user?.nickName ?: "",
                                        currentId = currentUserId,
                                        updateClick = {navController.navigate("${Routes.POST}/${post?.id}/${Routes.POST_EDIT}")},
                                        deletedClick = {
                                            coroutineScope.launch {
                                                val isSuccess = viewModel.deletePost(id)
                                                if (isSuccess){
                                                    navController.previousBackStackEntry?.savedStateHandle?.set("need_refresh", true)
                                                    navController.popBackStack() // 이전 화면으로 이동
                                                }
                                            }
                                        },
                                        navController = navController
                                    )
                                }
                                Text(post?.title ?: "", style = AppTextStyle.Title, )

                                Spacer(modifier = Modifier.height(20.dp))

                                if (post?.postImages?.isNotEmpty() == true && post != null){
                                    val pagerState = rememberPagerState(
                                        pageCount = { post?.postImages?.size ?: 0 }
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        HorizontalPager(
                                            state = pagerState
                                        ) { page ->
                                            val url = post?.postImages[page]?.imageUrl

                                            BaseAsyncImage(
                                                model = url,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop, // BoxFit.cover
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable(
                                                        onClick = {
                                                            selectedImage = post?.postImages[page]
                                                            isExpanded = true
                                                        }
                                                    )
                                            )
                                        }
                                    }
                                    DotsIndicator(
                                        dotCount = post?.postImages?.count() ?: 0,
                                        type = WormIndicatorType(
                                            dotsGraphic = DotGraphic(
                                                color = Color.Transparent,
                                                borderColor = OrangeColor,
                                                borderWidth = 1.5.dp,
                                                size = 10.dp),
                                            wormDotGraphic = DotGraphic(
                                                color = OrangeColor
                                            )
                                        ),
                                        pagerState = pagerState,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))

                                SelectionContainer {
                                    LinkifyText(
                                        text = post?.content ?: "",
                                        style = AppTextStyle.Content
                                    )
                                }

                            }
                            Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.clickable(onClick = {viewModel.likeToggle()}),
                                        imageVector = if (post?.isLiked == true)
                                            Icons.Default.Favorite
                                        else
                                            Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = if (post?.isLiked == true) Color.Red else Color.White
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        modifier = Modifier.clickable(onClick = {viewModel.fetchLikeUsers(post?.id ?:0)}),
                                        text = "${post?.likesCount ?: 0}",
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Row(
                                    modifier = Modifier.clickable(onClick = {
                                        viewModel.fetchComments(post?.id ?: 0)
                                    }),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ChatBubble,
                                        contentDescription = null,
                                        tint = Color.White
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = "${post?.commentsCount ?: 0}",
                                        color = Color.White
                                    )
                                }
                            }
                            if (selectPostId != null) {
                                when(bottomSheetType){
                                    BottomSheetType.COMMENT -> {
                                        CommentBottomSheet(
                                            viewModel= viewModel,
                                            screenHeight = screenHeight,
                                            sheetState = sheetState,
                                            onDismiss = {},
                                            navController = navController
                                        )
                                    }
                                    BottomSheetType.LIKES -> {
                                        LikeUserListBottomSheet(
                                            viewModel= viewModel,
                                            screenHeight = screenHeight,
                                            sheetState = sheetState,
                                            onDismiss = {},
                                            navController = navController
                                        )
                                    }
                                    null -> {}
                                }
                            }

                        }
                    } else {
                        // imageInputList에서 클릭한 사진의 인덱스를 찾아 초기 페이지로 설정
                        val pagerState = rememberPagerState(

                            initialPage = images.indexOf(selectedImage),
                            pageCount = { images.size }
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) {page ->
                                val currentUri = images[page].imageUrl
                                val zoomState = rememberZoomState()

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BaseAsyncImage(
                                        model = currentUri,
                                        contentDescription = "확대 이미지",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .sharedElement(
                                                rememberSharedContentState(key = "image_$currentUri"),
                                                animatedVisibilityScope = this@AnimatedContent,
                                            )
                                            .zoomable(zoomState)
                                    )
                                }
                            }
                            // 닫기 버튼
                            IconButton(
                                onClick = { isExpanded = false },
                                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "닫기", tint = Color.White)
                            }
                        }
                        // 안드로이드 뒤로가기 키 설정
                        BackHandler() {
                            if (isExpanded){
                                isExpanded = false
                            }
                        }
                    }
                }
            }
        }
    }
}
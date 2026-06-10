package com.newBie.new_bie.features.post.presentation.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.BaseAsyncImage
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.core.components.rememberPhotoPicker
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.features.post.presentation.components.ContentTextField
import com.newBie.new_bie.features.post.presentation.components.SelectedCategoryListLazyRow
import com.newBie.new_bie.features.post.presentation.components.TitleTextField
import com.newBie.new_bie.features.post.presentation.components.buttons.ImageDeleteButton
import com.newBie.new_bie.features.post.presentation.components.buttons.PostCategoryBtn
import com.newBie.new_bie.features.post.presentation.components.buttons.PostEditScreenActionButton
import com.newBie.new_bie.features.post.presentation.viewModels.PostAddViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.GreenColor
import com.newBie.new_bie.ui.theme.OrangeColor
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAddScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PostAddViewModel = viewModel<PostAddViewModel>(),
    notificationViewModel: NotificationViewModel,
    context: Context){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) //바텀 모달 시트 높이만큼 올라오게 만드는 설정
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var userInput by remember { mutableStateOf("") }
    val titleInput by viewModel.titleInputTxt.collectAsState()
    val contentInput by viewModel.contentInputTxt.collectAsState()
    val categoryList by viewModel.categoryList.collectAsState()
    val selectCategoryList by viewModel.selectCategoryList.collectAsState()
    val bottomSheetSelectedCategories by viewModel.bottomSheetSelectedCategories.collectAsState()
    val imageInputList by viewModel.imageInputList.collectAsState()
    val pickMultipleMedia = rememberPhotoPicker(10){
        viewModel.getImage(it)
    }
    // 꽉 차는 값 flag 값
    var isExpanded by remember { mutableStateOf(false) }
    // 사진 한장 한장가져올 값
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val focusManager = LocalFocusManager.current

    // 포스트 등록 인디케이터를 위한 플래그 값
    val isPosting by viewModel.isPosting.collectAsState()

    val isRead by notificationViewModel.isRead.collectAsState()

    // 등록에 성공했다면 홈 화면으로 이동하고 스택 없애기
    LaunchedEffect(Unit) {
        viewModel.postSuccess.collect { isSuccess ->
            if(isSuccess) {
                navController.popBackStack()
            }
        }
    }


    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBarLayout(
                title = "게시물 작성",
                focusManager = focusManager,
                navController = navController,
                isRead = isRead
            )
        },
    ) {innerPadding ->
        SharedTransitionLayout(
            modifier = Modifier
            .padding(
                top = innerPadding.calculateTopPadding(), // 상단만 적용
            )
            .pointerInput(Unit){
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }) {
            AnimatedContent(
                targetState = isExpanded,
                label = "ImageTransition"
            ) { targetExpended ->
                if (!targetExpended){
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(BlackColor)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)) {
                            TitleTextField(
                                modifier = Modifier.padding(bottom = 8.dp),
                                titleInput = titleInput,
                                onValueChange = {viewModel.onChangeTitleInput(it)}
                            )
                            Spacer(modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                            )
                            ContentTextField(
                                modifier = Modifier.weight(1f),
                                contentInput = contentInput,
                                onValueChange = {viewModel.onChangeContentInput(it)}
                            )
                            Column(modifier = Modifier.padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                if (imageInputList.isNotEmpty()){
                                    LazyRow(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                        items(imageInputList){
                                            Box(contentAlignment = Alignment.TopEnd){
                                                BaseAsyncImage(
                                                    model = it,
                                                    contentDescription = "미리보기 이미지",
                                                    modifier = Modifier
                                                        .size(90.dp)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .sharedElement(
                                                            rememberSharedContentState(key = "image_$it"),
                                                            animatedVisibilityScope = this@AnimatedContent
                                                        )
                                                        .clickable(onClick = {
                                                            selectedImage = it
                                                            isExpanded = true
                                                        })
                                                )
                                                ImageDeleteButton{ viewModel.deleteImage(it)}
                                            }
                                        }
                                    }
                                }
                                if (selectCategoryList.isNotEmpty()) {
                                    SelectedCategoryListLazyRow(selectCategoryList, {viewModel.unselectCategory(it)})
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                PostEditScreenActionButton(
                                    modifier = Modifier.weight(1f),
                                    Icons.Default.AddPhotoAlternate,
                                    "사진 추가",
                                    Color.Unspecified,
                                    onClick = pickMultipleMedia
                                )
                                PostEditScreenActionButton(
                                    modifier = Modifier.weight(1f),
                                    icon =Icons.Default.Label,
                                    contentDescription = "태그 추가",
                                    color = GreenColor,
                                    onClick = {
                                        viewModel.openBottomSheetCopyCategoriesList()
                                        showSheet = true
                                    }
                                )
                            }
                            Button(onClick = {
                                viewModel.insertPost(context = context)
                            },
                                enabled = titleInput.isNotEmpty() && contentInput.isNotEmpty() && !isPosting && selectCategoryList.isNotEmpty(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                colors = ButtonColors(
                                    containerColor = OrangeColor,
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.Gray,
                                    disabledContentColor = Color.White,
                                )
                            ) {
                                Text("저장")
                            }
//                            PostUpdateBtn(
//                                modifier = Modifier.padding(vertical = 10.dp),
//                                title = "등록",
//                                onClick = { viewModel.insertPost(context) }
//                            )
                        }

                        if (showSheet) {
                            ModalBottomSheet(
                                onDismissRequest = {showSheet = false},
                                sheetState = sheetState,
                                containerColor = BlackColor,
                                contentColor = Color.White,
                                dragHandle = {
                                    DragHandle(
                                        color = Color.Gray
                                    )
                                }
                            ) {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Button(modifier = Modifier.weight(0.5f),
                                        colors = ButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Color.Black,
                                            disabledContainerColor = Color.DarkGray,
                                            disabledContentColor = Color.White
                                        ),
                                        enabled = bottomSheetSelectedCategories.isNotEmpty(),
                                        onClick = {
                                            viewModel.clearCategories()
                                        }) {
                                        Text("선택 해제")
                                    }
                                    Button(modifier = Modifier.weight(0.5f),
                                        colors = ButtonColors(
                                            containerColor = GreenColor,
                                            contentColor = Color.White,
                                            disabledContainerColor = Color.DarkGray,
                                            disabledContentColor = Color.White
                                        ),
                                        onClick = {
                                            viewModel.confirmSelection()
                                            showSheet = false
                                        },
                                        enabled = bottomSheetSelectedCategories.isNotEmpty()
                                    ){
                                        Text("완료")
                                    }
                                }
                                LazyColumn() {
                                    items(categoryList){ item ->
                                        PostCategoryBtn(
                                            item,
                                            bottomSheetSelectedCategories.contains(item),
                                            {viewModel.toggleCategory(it)}
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // imageInputList에서 클릭한 사진의 인덱스를 찾아 초기 페이지로 설정
                    val pagerState = rememberPagerState(
                        initialPage = imageInputList.indexOf(selectedImage),
                        pageCount = { imageInputList.size }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.7f))
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) {page ->
                            val currentUri = imageInputList[page]
                            val zoomState = rememberZoomState()

                            Box(
                                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
        if(isPosting){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = false){},
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = OrangeColor)
            }
        }
    }
}
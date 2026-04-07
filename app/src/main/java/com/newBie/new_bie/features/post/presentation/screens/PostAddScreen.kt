package com.newBie.new_bie.features.post.presentation.screens

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.newBie.new_bie.core.components.BottomTapBar
import com.newBie.new_bie.core.components.TopBarTitleText
import com.newBie.new_bie.core.utils.PageSet
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.post.presentation.components.ContentTextField
import com.newBie.new_bie.features.post.presentation.components.SelectedCategoryListLazyRow
import com.newBie.new_bie.features.post.presentation.components.TitleTextField
import com.newBie.new_bie.features.post.presentation.components.buttons.ImageDeleteButton
import com.newBie.new_bie.features.post.presentation.components.buttons.PostEditCategoryBtn
import com.newBie.new_bie.features.post.presentation.components.buttons.PostEditScreenActionButton
import com.newBie.new_bie.features.post.presentation.components.buttons.PostUpdateBtn
import com.newBie.new_bie.features.post.presentation.viewModels.PostAddViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAddScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: PostAddViewModel = viewModel<PostAddViewModel>(), context: Context){
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
    val pickMultipleMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                viewModel.getImage(uris)
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


    // 등록에 성공했다면 홈 화면으로 이동하고 스택 없애기
    LaunchedEffect(Unit) {
        viewModel.postSuccess.collect { isSuccess ->
            if(isSuccess) {
                navController.navigate(Routes.HOME){
                    popUpTo(Routes.HOME) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BlackColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TopBarTitleText("게시물 작성")
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).weight(1f)) {
                TitleTextField(
                    modifier = Modifier.padding(bottom = 8.dp),
                    titleInput = titleInput,
                    onValueChange = {viewModel.onChangeTitleInput(it)}
                )
                Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(color = Color.DarkGray))
                ContentTextField(
                    modifier = Modifier.weight(1f),
                    contentInput = contentInput,
                    onValueChange = {viewModel.onChangeContentInput(it)}
                )
                Column(modifier = Modifier.padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (imageInputList.isNotEmpty()){
                        LazyRow(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(imageInputList){
                                Box(contentAlignment = Alignment.TopEnd){
                                    AsyncImage(
                                        model = it,
                                        contentDescription = "미리보기 이미지",
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable(onClick = {})// TODO: 사진 확대 로직 넣기
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
                    PostEditScreenActionButton(Icons.Default.Photo, "사진 추가", {pickMultipleMedia.launch( PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageAndVideo))})
                    PostEditScreenActionButton(Icons.Default.Label, "카테고리", {
                        viewModel.openBottomSheetCopyCategoriesList()
                        showSheet = true
                    })
                }
                PostUpdateBtn(modifier = Modifier.padding(vertical = 10.dp),title = "등록", onClick = {
                    viewModel.insertPost(context)
                })
            }
            BottomTapBar(navController, PageSet.ADD_POST)
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
                    Row(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                containerColor = OrangeColor,
                                contentColor = Color.White,
                                disabledContainerColor = Color.DarkGray,
                                disabledContentColor = Color.White
                            ),
                            onClick = {
                                viewModel.confirmSelection()
                                showSheet = false
                            }) {
                            Text("완료")
                        }
                    }
                    LazyColumn() {
                        items(categoryList){ item ->
                            PostEditCategoryBtn(
                                item,
                                bottomSheetSelectedCategories.contains(item),
                                {viewModel.toggleCategory(it)}
                            )
                        }
                    }
                }
            }
        }
    }
}
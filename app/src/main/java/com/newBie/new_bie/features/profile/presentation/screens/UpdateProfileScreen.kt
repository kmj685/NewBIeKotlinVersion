package com.newBie.new_bie.features.profile.presentation.screens

import android.content.Context
import android.graphics.drawable.Icon
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.newBie.new_bie.R
import com.newBie.new_bie.core.components.BaseAsyncImage
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.core.components.rememberPhotoPicker
import com.newBie.new_bie.core.components.rememberSinglePhotoPicker
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.profile.presentation.components.UpdateProfileOutlinedTextFiled
import com.newBie.new_bie.features.profile.presentation.viewModels.UpdateProfileViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor
import org.slf4j.MDC.clear

@Composable
fun UpdateProfileScreen(viewModel: UpdateProfileViewModel = viewModel<UpdateProfileViewModel>(), context: Context, navController: NavController){

    val focusManager = LocalFocusManager.current
    val user by viewModel.user.collectAsState()
    val openPhotoPicker = rememberSinglePhotoPicker() {
        viewModel.getImage(it)
    }
    val imageInput by viewModel.imageInput.collectAsState()
    val isImageDeleted by viewModel.isImageDeleted.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    // 업데이트에 성공했다면 UserProfile 페이지로 이동하고 스택 없애기
    LaunchedEffect(Unit) {
        viewModel.updateSuccess.collect { success ->
            if (success) {
                navController.navigate(Routes.MY_PROFILE){
                    popUpTo(Routes.MY_PROFILE) {inclusive = true} // inclusive는 자기 자신의 스택도 지울지 말지 처리하는거다.
                }
            }
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        topBar = { TopBarLayout("프로필 수정") }
    ) {innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .pointerInput(Unit){
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .background(BlackColor)
            .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Column(modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
                )
            {

                val imageSize = 160.dp
                val imageModifier = Modifier
                    .size(imageSize)
                    .clip(CircleShape)
                Box(){
                    // PhotoPicker에서 가져온 이미지가 우선순위로 바로 보이게
                    val selectedImage = when{
                        imageInput != null -> imageInput // 1순위: 방금 고른 사진
                        isImageDeleted == true -> null // 2순위: 삭제 버튼 눌렀으면 무조건 null
                        else -> user?.profileImage // 3순위: 둘 다 아니면 서버 사진
                    }


                        imageInput ?: user?.profileImage

                    if(selectedImage != null){
                        BaseAsyncImage(
                            model = selectedImage,
                            contentDescription = "프로필 이미지",
                            modifier = imageModifier,
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "유저 이미지",
                            modifier = imageModifier,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Button(
                        onClick = {
                            openPhotoPicker()
                        },
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.BottomEnd),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp), // 기본적인 padding을 빼야지 밑의 벡터 아이콘이 보인다.
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeColor,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            modifier = Modifier.size(23.dp),
                            contentDescription = "이미지 추가 아이콘",
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.deleteImage()
                        },
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.TopEnd),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp), // 기본적인 padding을 빼야지 밑의 벡터 아이콘이 보인다.
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            modifier = Modifier.size(30.dp),
                            contentDescription = "이미지 삭제 아이콘",
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))

                UpdateProfileOutlinedTextFiled(
                    state = viewModel.nicknameState,
                    labelText = "닉네임",
                    placeholderText = "닉네임을 입력해주세요",
                    isNicknameField = true
                )
                Spacer(Modifier.height(10.dp))
                UpdateProfileOutlinedTextFiled(
                    state = viewModel.introductionState,
                    labelText = "자기소개",
                    placeholderText = "자기소개를 입력해주세요"
                )
            }
            Button(onClick = {
                viewModel.saveUserProfile(
                    context = context
                )
            },
                enabled = viewModel.nicknameState.text.isNotEmpty() && !isSaving,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = OrangeColor,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White,
                )
            ) {
                Text("저장")
            }
        }
    }
    if(isSaving){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false){},
            contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = OrangeColor)
        }
    }
}
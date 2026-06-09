package com.newBie.new_bie.features.profile.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.ui.theme.GridColor
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun AddGuestbookBottomSheetTextField(
    state: TextFieldState,
    focusRequester: FocusRequester,
    focusManager: FocusManager){
    OutlinedTextField(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10))
            .focusRequester(focusRequester),
//            .background(color = Color(0xffF2F2F7FF), shape = RoundedCornerShape(10)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = GridColor,
            unfocusedContainerColor = GridColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = OrangeColor
        ),
        placeholder = { Text("내용을 입력해주세요!", fontSize = 18.sp) },
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
//        label = {Text("제목", color = Color.White)},
//            colors = OutlinedTextFieldDefaults.colors(),
//        trailingIcon = {
//            IconButton (
//                onClick = {
//                    if (state.text.isNotBlank()) {
//                        addPhoto.invoke()
//                        focusManager.clearFocus(true)
//                    }
//                },
//                colors = IconButtonDefaults.iconButtonColors(containerColor = OrangeColor),
//                modifier = Modifier.size(35.dp)
//            ){
//                Icon(
//                    imageVector = Icons.Default.AddPhotoAlternate,
//                    contentDescription = "사진 추가 버튼",
//                    tint = Color.White,
//                    modifier = Modifier
//                        .size(20.dp)
//                )
//            }
//        }
    )
}
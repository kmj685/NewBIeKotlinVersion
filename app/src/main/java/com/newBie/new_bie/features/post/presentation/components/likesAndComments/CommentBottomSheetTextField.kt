package com.newBie.new_bie.features.post.presentation.components.likesAndComments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.GridColor
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun CommentBottomSheetTextField(userCommentInput: String, onValueChange: (String) -> Unit, onSend:()-> Unit, focusRequester: FocusRequester, focusManager: FocusManager){
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
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
        value = userCommentInput,
        onValueChange = { onValueChange.invoke(it) },
        placeholder = { Text("댓글을 입력해주세요!", fontSize = 18.sp) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(
            onSend = {
                if (userCommentInput.isNotBlank()) {
                    onSend.invoke()
                    focusManager.clearFocus(true)
                }
            }
        ),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
//        label = {Text("제목", color = Color.White)},
//            colors = OutlinedTextFieldDefaults.colors(),
        trailingIcon = {
            IconButton (
                onClick = {
                    if (userCommentInput.isNotBlank()) {
                        onSend.invoke()
                        focusManager.clearFocus(true)
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = OrangeColor),
                modifier = Modifier.size(35.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "댓글 입력 버튼",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
    )
}
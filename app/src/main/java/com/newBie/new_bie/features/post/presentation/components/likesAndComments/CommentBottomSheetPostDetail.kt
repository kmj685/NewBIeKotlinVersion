package com.newBie.new_bie.features.post.presentation.components.likesAndComments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.core.components.BottomSheetTopBatTitle
import com.newBie.new_bie.core.utils.toKoreaLocalDateTime
import com.newBie.new_bie.core.utils.toTimeAgo
import com.newBie.new_bie.features.post.presentation.viewModels.PostDetailViewModel
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheetPostDetail(viewModel : PostDetailViewModel, screenHeight: Dp , sheetState: SheetState, onDismiss: () -> Unit){
    val commentsList by viewModel.comments.collectAsState()
    val userCommentInput by viewModel.userCommentInput.collectAsState()
    val selectedCommentId by viewModel.selectCommentId.collectAsState()
    val editUserCommentInput by viewModel.editUserCommentInput.collectAsState()

    ModalBottomSheet(
        containerColor = BlackColor,
        contentColor = BlackColor,
        onDismissRequest = {onDismiss.invoke()},
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = screenHeight * 0.7f, max = screenHeight * 0.7f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            BottomSheetTopBatTitle("댓글")
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(color = OrangeColor))
            if (commentsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("댓글이 없습니다.", fontSize = 20.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(modifier= Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    items(commentsList) { item ->
                        CommentItem(
                            modifier = Modifier.fillMaxWidth(),
                            imageUrl = item.user.profileImage,
                            commentId = item.id,
                            nickName = item.user.nickName?:"",
                            timeData = item.createdAt.toKoreaLocalDateTime().toTimeAgo(),
                            introduce = item.content,
                            userId = item.user.id,
                            onImageClick = {},
                            selectedId = selectedCommentId,
                            onSelect = {viewModel.onSelectComment(commentId = item.id, content = item.content?:"")},
                            onUpdateInput = {viewModel.updateEditUserInput(it)},
                            userInput = editUserCommentInput,
                            onCancel = {viewModel.onCancel()},
                            onUpdate = {
                                item.user.id.let{
                                    viewModel.editComment(it)
                                }
                            },
                            onDelete = {viewModel.deleteComment(item.id, item.user.id)}
                        )
                    }
                }
            }
            Row(

            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10))
                        .background(color = Color(0xffF2F2F7FF), shape = RoundedCornerShape(10)),
                    value = userCommentInput,
                    onValueChange = { viewModel.updateUserInput(it) },
                    placeholder = { Text("댓글") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (userCommentInput.isNotBlank()) {
                                // 검색 화면으로 이동 (작성하신 NavHost 경로 기준)
                                viewModel.insertComment()
                            }
                        }
                    ),
//            colors = OutlinedTextFieldDefaults.colors(),
                    trailingIcon = {
                        Icon(
                            Icons.Default.Send,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(onClick = {
                                    if (userCommentInput.isNotBlank()) {
                                        viewModel.insertComment()
                                    }
                                }),
                            contentDescription = null,
                            tint = BlackColor
                        )
                    }
                )
            }
        }

    }
}

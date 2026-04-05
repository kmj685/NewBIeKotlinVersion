//package com.newBie.new_bie.features.post.presentation.components.likesAndComments
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Send
//import androidx.compose.material3.Icon
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.ViewModel
//import com.newBie.new_bie.core.components.TopBarTitleText
//import com.newBie.new_bie.core.utils.toKoreaLocalDateTime
//import com.newBie.new_bie.core.utils.toTimeAgo
//import com.newBie.new_bie.ui.theme.BlackColor
//import com.newBie.new_bie.ui.theme.OrangeColor
//
//@Composable
//fun CommentBottomSheet(viewModel : ViewModel, ){
//    ModalBottomSheet(
//        containerColor = BlackColor,
//        contentColor = BlackColor,
//        onDismissRequest = {viewModel.unSelectPostId()},
//        sheetState = sheetState
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .heightIn(min = screenHeight * 0.5f, max = screenHeight * 0.5f),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            TopBarTitleText("댓글")
//            Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(color = OrangeColor))
//            if (commentsList.isEmpty()) {
//                Box(
//                    modifier = Modifier.fillMaxWidth().height(40.dp).weight(1f),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("댓글이 없습니다.", fontSize = 20.sp, color = Color.Gray)
//                }
//            } else {
//                LazyColumn(modifier= Modifier.fillMaxWidth().weight(1f)) {
//                    items(commentsList) { item ->
//                        CommentItem(
//                            modifier = Modifier.fillMaxWidth(),
//                            imageUrl = item.user.profileImage,
//                            nickName = item.user.nickName?:"",
//                            timeData = item.createdAt.toKoreaLocalDateTime().toTimeAgo(),
//                            introduce = item.content,
//                            userId = item.authorId,
//                            onImageClick = {}
//                        )
//                    }
//                }
//            }
//            Row(
//
//            ) {
//                OutlinedTextField(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(10))
//                        .background(color = Color(0xffF2F2F7FF), shape = RoundedCornerShape(10)),
//                    value = userCommentInput,
//                    onValueChange = { viewModel.updateUserInput(it) },
//                    placeholder = { Text("댓글") },
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                    keyboardActions = KeyboardActions(
//                        onSend = {
//                            if (userCommentInput.isNotBlank()) {
//                                // 검색 화면으로 이동 (작성하신 NavHost 경로 기준)
//                                viewModel.insertComment()
//                            }
//                        }
//                    ),
////            colors = OutlinedTextFieldDefaults.colors(),
//                    trailingIcon = {
//                        Icon(
//                            Icons.Default.Send,
//                            modifier = Modifier
//                                .size(20.dp)
//                                .clickable(onClick = {
//                                    if (userCommentInput.isNotBlank()) {
//                                        // 2. 돋보기 아이콘 클릭 시 이동
//                                        viewModel.insertComment()
//                                    }
//                                }),
//                            contentDescription = null,
//                            tint = BlackColor
//                        )
//                    }
//                )
//            }
//        }
//
//    }
//}
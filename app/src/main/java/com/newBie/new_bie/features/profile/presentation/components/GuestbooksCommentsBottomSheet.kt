package com.newBie.new_bie.features.profile.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.core.components.BottomSheetTopBatTitle
import com.newBie.new_bie.core.utils.toKoreaLocalDateTime
import com.newBie.new_bie.core.utils.toTimeAgo
import com.newBie.new_bie.features.post.presentation.components.likesAndComments.CommentBottomSheetTextField
import com.newBie.new_bie.features.post.presentation.components.likesAndComments.CommentItem
import com.newBie.new_bie.features.profile.presentation.viewModels.GuestbooksCommentsBottomSheetViewModel
import com.newBie.new_bie.ui.theme.BlackColor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestBooksCommentsBottomSheet(viewModel : GuestbooksCommentsBottomSheetViewModel, screenHeight: Dp, sheetState: SheetState, onDismiss: () -> Unit){
    val commentsList by viewModel.guestbooksComments.collectAsState()
    val userCommentInput by viewModel.userCommentInput.collectAsState()
    val selectedCommentId by viewModel.selectCommentId.collectAsState()
    val editUserCommentInput by viewModel.editUserCommentInput.collectAsState()

    // 포커스를 잡을때 필요한 상태정보
    val addCommentFocusRequester = remember { FocusRequester() }
    val editCommentFocusRequester = remember { FocusRequester() }
    // 포커스 매니저는 포커스를 해제할 때 사용됨(여기서는)
    val focusManager = LocalFocusManager.current

    ModalBottomSheet(
        containerColor = BlackColor,
        contentColor = BlackColor,
        onDismissRequest = {
            viewModel.unSelectPostId()
            onDismiss()
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = screenHeight * 0.7f, max = screenHeight * 0.7f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            BottomSheetTopBatTitle("댓글")
//            HorizontalDivider(
//                thickness = 0.5.dp,
//                color = Color.LightGray,
//                modifier = Modifier
//                    .padding(5.dp)
//            )
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
                    items(commentsList) {item ->
                        if (item != null){
                            CommentItem(
                                modifier = Modifier.fillMaxWidth(),
                                imageUrl = item.authorId.profileImage,
                                commentId = item.id,
                                nickName = item.authorId.nickName ?:"",
                                timeData = item.createdAt.toKoreaLocalDateTime().toTimeAgo(),
                                introduce = item.content,
                                userId = item.authorId.id,
                                onImageClick = {},
                                selectedId = selectedCommentId,
                                onSelect = {viewModel.onSelectComment(commentId = item.id, content = item.content?:"")},
                                onUpdateInput = {viewModel.updateEditUserInput(it)},
                                userInput = editUserCommentInput,
                                onCancel = {viewModel.onCancel()},
                                onUpdate = {
                                    item.authorId.id.let{
                                        viewModel.editComment(it)
                                    }
                                },
                                onDelete = {viewModel.deleteComment(item.id, item.authorId.id)},
                                focusManager = focusManager,
                                focusRequester = editCommentFocusRequester
                            )
                        }

                    }
                }
            }
            Row(

            ) {
                CommentBottomSheetTextField(
                    userCommentInput = userCommentInput,
                    onValueChange = {viewModel.updateUserInput(it)},
                    onSend = {viewModel.insertComment()},
                    focusRequester = addCommentFocusRequester,
                    focusManager= focusManager
                )
            }
        }

    }
}
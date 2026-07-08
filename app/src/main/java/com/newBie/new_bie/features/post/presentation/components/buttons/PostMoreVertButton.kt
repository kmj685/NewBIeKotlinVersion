package com.newBie.new_bie.features.post.presentation.components.buttons

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.block.presentation.BlockUserViewModel
import com.newBie.new_bie.core.components.ReportDialog
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun PostMoreVertButton(targetId: String,
                       targetIdNickname: String?,
                       currentId: String,
                       updateClick: () -> Unit,
                       deletedClick: () -> Unit,
                       viewModel: BlockUserViewModel = hiltViewModel(),
                       navController: NavController,
                       guestbooksMode: Boolean = false,
                       hostId: String? = null
){

    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val showReportDialog by viewModel.showReportDialog.collectAsState()

    Box(){
        IconButton(
            onClick = {expanded = !expanded} // 토글로
        ){
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "더보기 버튼",
                tint = OrangeColor
            )
        }
        if (targetId == currentId){
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                if (!guestbooksMode){
                    DropdownMenuItem(
                        text = {
                            Text("수정")
                        },
                        onClick = {
                            expanded = false
                            updateClick.invoke()
                        }
                    )
                }
                DropdownMenuItem(
                    text = {
                        Text("삭제")
                    },
                    onClick = {
                        expanded = false
                        deletedClick.invoke()
                    }
                )
            }
        }
        if (targetId != currentId){
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                DropdownMenuItem(
                    text = {
                        Text("차단")
                    },
                    onClick = {
                        expanded = false
                        viewModel.insertBlockUser(
                            targetId = targetId
                        )
                        Toast.makeText(context, "${targetIdNickname}님을 성공적으로 차단했습니다.", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text("신고")
                    },
                    onClick = {
                        expanded = false
                        viewModel.showReportDialog(true)
                    }
                )
                if (guestbooksMode && currentId == hostId){
                    DropdownMenuItem(
                        text = {
                            Text("방명록 삭제")
                        },
                        onClick = {
                            expanded = false
                            deletedClick.invoke()
                        }
                    )
                }
            }
        }
    }
    if (showReportDialog) {
        ReportDialog(
            onConfirm = { category, content ->
                viewModel.insertReportUser(
                    reportedId = targetId,
                    category = category,
                    content = content.ifBlank { null } // 아무것도 안 적었으면 null 처리
                )
                Toast.makeText(context, "신고가 성공적으로 접수되었습니다.", Toast.LENGTH_SHORT).show()
            },
            onDismissRequest = {
                // 💡 취소하거나 바깥 누르면 다이얼로그를 닫습니다.
                viewModel.showReportDialog(false)
            }
        )
    }
}
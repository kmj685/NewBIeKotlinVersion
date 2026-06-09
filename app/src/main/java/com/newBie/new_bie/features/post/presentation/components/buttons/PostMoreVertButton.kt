package com.newBie.new_bie.features.post.presentation.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun PostMoreVertButton(targetId: String, currentId: String, updateClick: () -> Unit, deletedClick: () -> Unit){

    var expanded by remember { mutableStateOf(false) }

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
                DropdownMenuItem(
                    text = {
                        Text("게시글 수정")
                    },
                    onClick = {
                        expanded = false
                        updateClick.invoke()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text("게시글 삭제")
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
                        // TODO: 사용자 차단 로직
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text("신고")
                    },
                    onClick = {
                        expanded = false
                        // TODO: 사용자 신고 로직
                    }
                )
            }
        }
    }
}
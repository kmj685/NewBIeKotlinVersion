package com.newBie.new_bie.features.post.presentation.components.likesAndComments

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.newBie.new_bie.R
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.ui.theme.AppTextStyle
import com.newBie.new_bie.ui.theme.OrangeColor
import io.github.jan.supabase.auth.auth

@Composable
fun CommentItem(
    modifier: Modifier,
    imageUrl : String?,
    commentId: Int,
    nickName : String,
    timeData : String,
    introduce : String?,
    userId : String?,
    onImageClick : () -> Unit,
    onSelect:()-> Unit,
    onDelete: ()-> Unit={},
    selectedId: Int?,
    onUpdateInput:(String) -> Unit = {},
    userInput: String,
    onCancel: () -> Unit,
    onUpdate:()-> Unit,
    focusManager: FocusManager,
    focusRequester: FocusRequester

) {
    val imageSize = 60.dp
    val currentUserId = SupabaseManager.supabase.auth.currentUserOrNull()?.id

    val imageModifier = Modifier
        .size(imageSize)
        .clip(CircleShape)
        .clickable(enabled = userId != null) {
            onImageClick.invoke()
        }


    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
        modifier = modifier.padding(8.dp)
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "네트워크 이미지",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "유저 이미지",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1F)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(nickName, color = OrangeColor)
                Text(timeData, style = AppTextStyle.Date)
            }

            if (selectedId == commentId) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { onUpdateInput.invoke(it) },
                        modifier = Modifier.weight(1f).focusRequester(focusRequester),
                        textStyle = TextStyle(color = Color.White),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (userInput.isNotBlank()) {
                                    onUpdate.invoke()
                                    focusManager.clearFocus(true)
                                }
                            }
                        ),

                    )
                    IconButton(onClick = {
                        onUpdate.invoke()
                        focusManager.clearFocus(true)
                    }) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Green)
                    }
                    IconButton(onClick = {
                        onCancel.invoke()
                        focusManager.clearFocus(true)
                    }) {
                        Icon(Icons.Default.Cancel, contentDescription = null, tint = Color.Red)
                    }
                }
            } else{
                Text(introduce ?: "", color = Color.White)
            }
        }


        var expanded by remember { mutableStateOf(false) }
        if (selectedId == null){
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = OrangeColor
                    )
                }
                if (userId == currentUserId) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("수정") },
                            onClick = {
                                expanded=false
                                onSelect()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("삭제") },
                            onClick = {
                                expanded=false
                                onDelete.invoke()
                            }
                        )
                    }
                } else {
                    Log.d(Constants.TAG, "currentId: ${currentUserId}")
                    Log.d(Constants.TAG, "userId: ${userId} ")
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("신고") },
                            onClick = { /* Do something... */ }
                        )
                        DropdownMenuItem(
                            text = { Text("차단") },
                            onClick = { /* Do something... */ }
                        )
                    }
                }

            }
        }
    }
}
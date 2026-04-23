package com.newBie.new_bie.features.profile.presentation.components

import android.opengl.Matrix.length
import android.text.TextUtils.replace
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun UpdateProfileOutlinedTextFiled(state: TextFieldState, labelText: String, placeholderText: String, isNicknameField: Boolean = false){
    var isFocused by remember { mutableStateOf(false) }
    val hasSpace = isNicknameField && state.text.toString().contains(" ")


        OutlinedTextField(
            state = state,
            modifier = Modifier.fillMaxWidth().onFocusChanged{ focusState ->
                isFocused = focusState.isFocused
            },
            label =  { Text(text = labelText, color = Color.White)},
            placeholder = { Text(text = placeholderText)},
            trailingIcon = {
                IconButton(onClick = { state.edit { replace(0, length, "") }}) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "지우기",
                        tint = if(isFocused){
                            OrangeColor
                        } else {
                            Color.Gray
                        }
                    )
                }
            },
            isError = hasSpace,
            supportingText = {
                if (hasSpace){
                    Text(
                        text = "공백은 포함할 수 없습니다.",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            },
            lineLimits = TextFieldLineLimits.SingleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = OrangeColor,
                unfocusedLabelColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = OrangeColor
            )
    )
}
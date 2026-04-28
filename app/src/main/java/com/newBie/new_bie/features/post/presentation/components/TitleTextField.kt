package com.newBie.new_bie.features.post.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.newBie.new_bie.core.components.PlaceholderText
import com.newBie.new_bie.ui.theme.GridColor
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun TitleTextField(modifier: Modifier = Modifier, titleInput : String, onValueChange : (String) -> Unit) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = titleInput,
        onValueChange = {onValueChange.invoke(it)},
        textStyle = TextStyle(color = Color.White),
        label = {Text("제목", color = Color.White)},
        placeholder = { PlaceholderText("제목을 입력하세요") },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = GridColor,
            focusedContainerColor = GridColor,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = OrangeColor,
            cursorColor = OrangeColor
        )
    )
}
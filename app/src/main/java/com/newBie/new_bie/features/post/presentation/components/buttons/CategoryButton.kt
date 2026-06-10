package com.newBie.new_bie.features.post.presentation.components.buttons


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.newBie.new_bie.ui.theme.GreenColor
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun CategoryButton(title : String, selectedCategory : String, onSelect : () -> Unit) {
    OutlinedButton(
        modifier = Modifier.padding(end = 8.dp, bottom = 4.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 8.dp
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = btnBgColor(title, selectedCategory),
            contentColor = Color.White
        ),
        border = BorderStroke(
            color = GreenColor,
            width = 1.dp,
        ),
        //
        onClick = {onSelect.invoke()},
        ) {
        Text(title, color = Color.White)
    }
}

fun btnBgColor(title : String, selectedCategory : String) : Color {
    if (title == selectedCategory) return GreenColor
    else return Color.Transparent
}
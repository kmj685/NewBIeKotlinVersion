package com.newBie.new_bie.features.post.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun PostUpdateBtn(modifier: Modifier = Modifier, title : String, onClick : () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(color = OrangeColor)
            .border(width = 1.dp, color = OrangeColor, shape = RoundedCornerShape(50))
            .clickable(onClick = {onClick.invoke()})
            .padding(vertical = 12.dp)
    ) {
        Text(title, color = Color.White)
    }
}
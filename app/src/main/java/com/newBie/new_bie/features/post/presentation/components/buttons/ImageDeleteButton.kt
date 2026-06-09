package com.newBie.new_bie.features.post.presentation.components.buttons

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.features.post.domain.entities.PostImageEntity

@Composable
fun ImageDeleteButton(onClick: () -> Unit){
    Box(modifier = Modifier
        .clip(CircleShape)
        .background(Color.Red)
        .clickable(onClick = {onClick.invoke()})
        .padding(5.dp)
        .size(15.dp),
        contentAlignment = Alignment.Center){
        Icon(
            imageVector = Icons.Rounded.Close,
            tint = Color.White,
            contentDescription = "이미지 삭제버튼",
        )
    }
}
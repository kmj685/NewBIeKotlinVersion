package com.newBie.new_bie.features.post.presentation.components.buttons


import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Month

@Composable
fun PostEditScreenActionButton (icon: ImageVector, title : String, onClick : () -> Unit = {}) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color = Color.White)
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(50))
            .clickable(onClick = {onClick.invoke()})
            .padding(horizontal = 12.dp, vertical = 8.dp)

    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(16.dp))
        Text(title, color = Color.DarkGray, fontSize = 16.sp)
    }
}
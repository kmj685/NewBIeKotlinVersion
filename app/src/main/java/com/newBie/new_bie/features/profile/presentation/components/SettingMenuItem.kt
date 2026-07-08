package com.newBie.new_bie.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.ui.theme.GridColor

@Composable
fun SettingMenuItem(
    text: String,
    createdAt: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    textAlign: TextAlign = TextAlign.Start
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .clip(RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null){
            Icon(
                imageVector = icon,
                contentDescription = "이동",
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(15.dp))
        Row() {
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = textAlign
            )
            if (createdAt != null) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = createdAt,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
package com.newBie.new_bie.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object AppTextStyle {
    val Title = TextStyle(
        color = OrangeColor,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )

    val Content = TextStyle(
        color = Color.White,
        fontSize = 16.sp
    )

    val PageTitle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )

    val Date = TextStyle(
        color = Color.White.copy(alpha = 0.54f),
        fontSize = 14.sp
    )

    val Button = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )

    val Count = TextStyle(
        fontSize = 14.sp
    )
}
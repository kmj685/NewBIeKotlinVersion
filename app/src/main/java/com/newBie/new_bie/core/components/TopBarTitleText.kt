package com.newBie.new_bie.core.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun TopBarTitleText(title: String) {
    Text(title, modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),color = OrangeColor, fontSize = 22.sp, fontWeight = FontWeight.Bold)

}
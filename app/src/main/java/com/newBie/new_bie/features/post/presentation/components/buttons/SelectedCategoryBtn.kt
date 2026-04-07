package com.newBie.new_bie.features.post.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun SelectedCategoryBtn(category: CategoryTypeEntity, onClick : (CategoryTypeEntity) -> Unit = {}) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(30))
            .background(OrangeColor)
            .clickable(onClick = {onClick.invoke(category)})
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text(category.typeTitle, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight(600))
    }
}
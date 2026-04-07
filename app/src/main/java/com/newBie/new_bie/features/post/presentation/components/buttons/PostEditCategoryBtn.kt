package com.newBie.new_bie.features.post.presentation.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.MainScreen
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun PostEditCategoryBtn(category: CategoryTypeEntity, isSelected : Boolean, onClick : (CategoryTypeEntity) -> Unit = {}) {
    val icon = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked

    Row(
        modifier = Modifier.clickable(onClick = {onClick.invoke(category)}).padding(14.dp)
    ) {
        Text(category.typeTitle, fontSize = 18.sp, modifier = Modifier.weight(1f))
        Icon(icon, contentDescription = null, tint = OrangeColor)
    }
}
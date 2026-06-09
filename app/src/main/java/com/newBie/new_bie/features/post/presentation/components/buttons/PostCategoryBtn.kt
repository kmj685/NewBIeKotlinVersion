package com.newBie.new_bie.features.post.presentation.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.ui.theme.GreenColor

@Composable
fun PostCategoryBtn(category: CategoryTypeEntity, isSelected : Boolean,
                    onClick : (CategoryTypeEntity) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable(onClick = {onClick.invoke(category)})
            .padding(14.dp)
            .heightIn(min = 35.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(category.typeTitle, fontSize = 18.sp, modifier = Modifier.weight(1f))
        if(isSelected){
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GreenColor)
        }
    }
}
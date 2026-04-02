package com.newBie.new_bie.features.post.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.presentation.components.buttons.SelectedCategoryBtn
import com.newBie.new_bie.features.post.presentation.viewModels.PostAddViewModel

@Composable
fun SelectedCategoryListLazyRow(selectCategoryList : List<CategoryTypeEntity>, onClick : (CategoryTypeEntity) -> Unit ) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(selectCategoryList) {
            SelectedCategoryBtn(it, {onClick.invoke(it)})
        }
    }
}
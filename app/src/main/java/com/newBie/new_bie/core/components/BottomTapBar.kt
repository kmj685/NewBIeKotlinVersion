package com.newBie.new_bie.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.newBie.new_bie.core.utils.PageSet

@Composable
fun BottomTapBar(navController: NavController, pageSet: PageSet) {
    fun checkPageSet(pageSet: PageSet, currentPage : PageSet) : Boolean {
        return pageSet == currentPage
    }

    Row(horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
//            .background(color = Color(0xffE8DFCA))
            .padding(10.dp)
    ) {
        AppBarButton(PageSet.HOME, "",checkPageSet(PageSet.HOME, pageSet), navController)
//        AppBarButton(PageSet.GROUP, "",checkPageSet(PageSet.GROUP, pageSet), navController)
        AppBarButton(PageSet.ADD_POST, "",checkPageSet(PageSet.ADD_POST, pageSet), navController)
        AppBarButton(PageSet.PROFILE, "",checkPageSet(PageSet.PROFILE, pageSet), navController)
    }
}
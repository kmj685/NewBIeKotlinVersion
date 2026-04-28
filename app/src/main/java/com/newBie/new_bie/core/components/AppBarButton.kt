package com.newBie.new_bie.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.newBie.new_bie.core.utils.PageSet
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.OrangeColor
import kotlinx.coroutines.launch

@Composable
fun AppBarButton(pageSet: PageSet, title : String,
                 isSelected : Boolean = false,
                 navController: NavController ) {
    val icon = when (pageSet) {
        PageSet.HOME -> Icons.Default.Home
        PageSet.GROUP -> Icons.Default.Diversity3
        PageSet.ADD_POST -> Icons.Default.Add
        PageSet.PROFILE -> Icons.Default.Person
    }

//    var smallFontSet by remember { mutableStateOf(false) }
//    LaunchedEffect(Unit) {
//        launch {
//            SettingRepository.fontSizeSetFlow.collect{
//                smallFontSet = it
//            }
//        }
//    }
//    fun setContentFontSize(isOk : Boolean) : TextUnit {
//        if (isOk == true) return appbarButtonSmallSize else return appbarButtonNormalSize
//    }
//    val navRoot = when (pageSet) {
//        PageSet.SETTING -> {navController.navigate("setting")}
//        PageSet.RECORD -> {navController.navigate("questRecord")}
//        PageSet.QUEST -> {navController.navigate("main")}
//    }
    val iconColor = if (isSelected) OrangeColor else Color.White
//    val buttonBGColor = if (isSelected) Color(0xff6D94C5) else Color(0xffF5EFE6)
    val buttonBGColor = BlackColor
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = buttonBGColor)
            .clickable(enabled = !isSelected, onClick = {
                when (pageSet) {
                    PageSet.HOME -> {
                        navController.navigate(Routes.HOME){
                            // 기존 화면 스택들 모두 날리기
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            // 화면 하나만 나오게 처리
                            launchSingleTop = true
                        }
                    }
                    PageSet.ADD_POST -> {
                        navController.navigate(Routes.ADD){
                            // 기존 화면 스택들 모두 날리기
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            // 화면 하나만 나오게 처리
                            launchSingleTop = true
                        }
                    }
                    PageSet.PROFILE -> {
                        navController.navigate(Routes.MY_PROFILE){
                            // 기존 화면 스택들 모두 날리기
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            // 화면 하나만 나오게 처리
                            launchSingleTop = true
                        }
                    }
                    PageSet.GROUP -> {
                        navController.navigate(Routes.TEAM_PROJECT){
                            // 기존 화면 스택들 모두 날리기
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            // 화면 하나만 나오게 처리
                            launchSingleTop = true
                        }
                    }
                }
            })
            .padding(10.dp)
    ) {
        Icon(icon, contentDescription = null,tint = iconColor)
//        Text(title, color = iconColor, fontWeight = FontWeight.Bold, )
    }
}
package com.newBie.new_bie.core.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun BackOnBackPressed(onFinish: () -> Unit){
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current

    BackHandler() {
        val currentTime = System.currentTimeMillis()
        // 2초 이내에 다시 누르면 종료
        if (currentTime - backPressedTime < 2000) {
            onFinish()
        } else {
            backPressedTime = currentTime
            Toast.makeText(context, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
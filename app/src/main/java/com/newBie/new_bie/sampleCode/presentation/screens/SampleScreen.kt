package com.newBie.new_bie.sampleCode.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newBie.new_bie.sampleCode.presentation.viewModel.SampleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ContextCastToActivity")
@Composable
fun SampleScreen(modifier: Modifier = Modifier, viewModel: SampleViewModel = viewModel<SampleViewModel>()){

    val sample by viewModel.sampleInfo.collectAsState()

    val currentActivity = LocalContext.current as Activity
    val context = LocalContext.current

    var isRefreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            scope.launch {
//                viewModel.refresh()
                delay(2000)

                isRefreshing = false
            }
        },
        modifier = modifier
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(1) {
//                ProfileView(modifier,user)
                Text("1")
            }
        }
    }

//    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
//        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
//
//        }
//    }

}
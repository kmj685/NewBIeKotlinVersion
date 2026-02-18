package com.newBie.new_bie.features.auth.presentation.screens

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.utils.findActivity
import com.newBie.new_bie.features.auth.presentation.viewModels.AuthViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: AuthViewModel = viewModel<AuthViewModel>()) {
    val context = LocalContext.current
    val activity = context.findActivity()

    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Button(onClick = {viewModel.signInWithGoogle(activity)}) {
            Text("구글 로그인")
        }
    }
}
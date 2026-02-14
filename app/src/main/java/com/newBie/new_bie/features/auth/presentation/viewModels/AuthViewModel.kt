package com.newBie.new_bie.features.auth.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.features.auth.presentation.states.AuthState
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initializing)
    val authState: StateFlow<AuthState> = _authState

    init {
//        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            SupabaseManager.supabase.auth.sessionStatus.collectLatest { status ->
                when (status) {

                    is SessionStatus.Authenticated -> {

                    }

                    SessionStatus.Initializing -> println("Initializing")
                    is SessionStatus.RefreshFailure -> {
                        println("Session expired and could not be refreshed")
                    }

                    is SessionStatus.NotAuthenticated ->{

                    }
                }


            }
        }
    }


    fun signInWithGoogle() {
        viewModelScope.launch {
            try {
                SupabaseManager.googleSignIn()
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }
}
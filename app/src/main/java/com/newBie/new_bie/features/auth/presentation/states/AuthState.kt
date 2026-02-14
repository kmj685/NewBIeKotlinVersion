package com.newBie.new_bie.features.auth.presentation.states

sealed interface AuthState {
    object Initializing : AuthState
    object Unauthenticated : AuthState
    object Authenticated : AuthState
    object SignedOut : AuthState
    object SessionExpired : AuthState
}
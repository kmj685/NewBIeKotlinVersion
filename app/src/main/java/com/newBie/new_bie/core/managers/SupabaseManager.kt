package com.newBie.new_bie.core.managers

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.messaging.FirebaseMessaging
import com.newBie.new_bie.App
import com.newBie.new_bie.core.utils.SupabaseInitial
import com.newBie.new_bie.features.post.data.dto.UserDto
import com.newBie.new_bie.features.post.data.mapper.toEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Github
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import java.security.MessageDigest
import java.util.UUID
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object SupabaseManager {
    val context = App.instance



    val supabase = createSupabaseClient(
        supabaseUrl = SupabaseInitial.URL,
        supabaseKey = SupabaseInitial.ANON_KEY
    ) {

        // AndroidManifest.xml에서 설정한 딥링크 callback 주소 입력
        install(Auth) {
            scheme = "newbie"
            host = "login-callback"
        }
        install(Postgrest)
        install(Storage)
    }



    suspend fun googleSignIn(activity: Activity) {
        val credentialManager = CredentialManager.create(activity)

        val rawNonce = UUID.randomUUID().toString()
        val hashedNonce = MessageDigest.getInstance("SHA-256")
            .digest(rawNonce.toByteArray())
            .joinToString("") { "%02x".format(it) }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(SupabaseInitial.GOOGLE_WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = activity
        )

        val googleCredential =
            GoogleIdTokenCredential.createFrom(result.credential.data)

        supabase.auth.signInWith(IDToken) {
            idToken = googleCredential.idToken
            provider = Google
            nonce = rawNonce
        }

        //FCM 토큰 업데이트
        val userId = supabase.auth.currentUserOrNull()?.id
        if (userId != null) {
            // FCM 토큰을 가져와서 DB에 저장
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                CoroutineScope(Dispatchers.IO).launch {
                    updateFcmToken(userId, token)
                }
            }
        }
    }

    suspend fun githubSignIn() {
        supabase.auth.signInWith(Github)

        //FCM 토큰 업데이트
        val userId = supabase.auth.currentUserOrNull()?.id
        if (userId != null) {
            // FCM 토큰을 가져와서 DB에 저장
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                CoroutineScope(Dispatchers.IO).launch {
                    updateFcmToken(userId, token)
                }
            }
        }
    }

    suspend fun logout(){
        supabase.auth.signOut()
    }

    // FCM 토큰 Supabase에 보내는 함수
    suspend fun updateFcmToken(userId: String, token: String) {

        if (userId.isEmpty() || token.isEmpty()) {
            Log.d("FCM_TOKEN", "유효하지 않은 유저 ID 또는 토큰입니다.")
            return
        }
        try {
            SupabaseManager.supabase.from("users").update(
                mapOf("fcm_token" to token)
            ) {
                filter {
                    eq("id", userId) // 현재 로그인한 유저의 ID와 일치하는 행만 업데이트
                }
            }
            Log.d("FCM_TOKEN", "토큰 업데이트 성공: $token")
        } catch (e: Exception) {
            Log.e("FCM_TOKEN", "토큰 업데이트 실패", e)
        }
    }
}
package com.newBie.new_bie

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.newBie.new_bie.core.components.BackOnBackPressed
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.managers.SupabaseManager.updateFcmToken
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.ui.theme.BlackColor
import com.newBie.new_bie.ui.theme.NewBieTheme
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.handleDeeplinks
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // 1. 클래스 내부 상단으로 이동
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) { Log.d("FCM_LOG", "권한 허용됨") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 딥링크 처리 (앱이 처음 실행되면서 인텐트를 받을 때)
        SupabaseManager.supabase.handleDeeplinks(intent)

        // 알림 설정 물어보기
        askNotificationPermission()

        // 알림 채널 생성
        createNotificationChannel()

        // 현재 기기의 토큰을 가져온 Supabase에 업데이트하는 함수
        val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
        if (!userId.isNullOrEmpty()) { // null 처리
            fetchAndUploadToken(userId)
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.dark(
                Color.TRANSPARENT
            )
        )

        setContent {
            // 두 번 눌러 앱 종료
            BackOnBackPressed {
                finish()
            }
            NewBieTheme {
                // 현재 intent를 state로 관리 → onNewIntent 시 recomposition 유발
                var currentIntent by remember { mutableStateOf(intent) }

                // onNewIntent에서 업데이트된 intent를 MainScreen에 전달하기 위해 저장
                LaunchedEffect(Unit) {
                    intentHolder = { currentIntent = it }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.background(BlackColor).padding(innerPadding),
                        notificationIntent = currentIntent
                    )
                }
            }
        }
    }

    // Compose에서 새 intent를 전달받기 위한 콜백
    private var intentHolder: ((Intent) -> Unit)? = null

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // 딥링크 처리 (앱이 이미 실행된 상태에서 인텐트를 받을 때)
        SupabaseManager.supabase.handleDeeplinks(intent)
        // Compose state 업데이트 → MainScreen recomposition
        intentHolder?.invoke(intent)
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // 토큰 가져오기
    fun fetchAndUploadToken(currentUserId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { token ->
            if (!token.isSuccessful) {
                Log.w("FCM_LOG", "토큰 가져오기 실패", token.exception)
                return@addOnCompleteListener
            }

            // 1. 구글로부터 토큰을 성공적으로 가져옴
            val token = token.result

            // 2. 코루틴을 사용해 Supabase에 업데이트
            lifecycleScope.launch {
                updateFcmToken(currentUserId, token)
            }
        }
    }

    // 알림 채널 설정
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 엣지 펑션에서 내용의 텍스트를 관리하고 있기 때문에 여기서의 내용(name. descriptionText) 들은 보험 같은 느낌
            val name = "게시글 알림"
            val descriptionText = "팔로우한 작가의 새 게시글 알림을 받습니다."
            val importance = NotificationManager.IMPORTANCE_HIGH // 소리와 팝업을 위해 HIGH 설정
            val channel = NotificationChannel("post_channel", name, importance).apply {
                description = descriptionText
            }

            // 시스템에 채널 등록
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
package com.newBie.new_bie.core.managers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.newBie.new_bie.MainActivity
import com.newBie.new_bie.R
import com.newBie.new_bie.core.managers.SupabaseManager.updateFcmToken
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token) // super 호출 권장
        Log.d("FCM_LOG", "Refreshed token: $token")
        sendRegistrationToServer(token)
    }
    // 서버 전송 로직 place holder 코드
    private fun sendRegistrationToServer(token: String) {

        // 현재 로그인된 유저가 있다면 바로 업데이트
        // (로그인 정보가 세션이나 DataStore에 저장되어 있어야 함)
        val currentUserId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
        if (currentUserId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                updateFcmToken(currentUserId, token)
            }
        } else {
            Log.d("FCM_LOG", "로그인 상태가 아니라서 서버에 토큰을 저장하지 않았습니다.")
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 1. 데이터 추출
        val postId = remoteMessage.data["postId"]
        val followerId = remoteMessage.data["followerId"]
        val guestbookId= remoteMessage.data["guestbookId"]
        val type = remoteMessage.data["type"]

        Log.d("FCM_LOG", "받은 데이터 - postId: $postId, followerId: $followerId")

        // 2. 알림 클릭 시 이동할 Intent 설정
        val intent = Intent(this, MainActivity::class.java).apply {
            // 앱이 실행 중일 때 기존 액티비티를 재사용하도록 설정
            action = Intent.ACTION_VIEW // 이 인텐트의 목적이 '콘텐츠 보기'임을 명시합니다.

            // 중요: 앱이 이미 켜져 있다면 새로 만들지 말고 기존 화면을 재사용하라는 명령입니다.
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // 이동할 화면에 전달할 '택배 물건'들을 인텐트 주머니에 넣습니다.
            putExtra("postId", postId)
            putExtra("followerId", followerId)
            putExtra("guestbookId", guestbookId)
            putExtra("type", type)
        }

        // 당장 실행하는 게 아니라, 나중에 유저가 알림을 클릭했을 때 인텐트를 실행하도록 대행권을 만듭니다.
        val pendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 4. NotificationManager를 통해 실제 알림 띄우기 (채널 설정 필수)
        // 만약 remoteMessage.notification이 null이라면 data에서 직접 꺼내 써야 합니다.

        // 알림창에 뜰 제목과 내용을 결정합니다. (알림 메시지가 없으면 데이터에서, 그것도 없으면 기본값 사용)
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "New Post"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "Check this out!"

        // 실제 알림창의 디자인과 기능을 조립
        val notificationBuilder = NotificationCompat.Builder(this, "post_channel")
            .setSmallIcon(R.mipmap.ic_launcher) // 상단바에 뜰 아이콘 설정
            .setContentTitle(title) // 제목 설정
            .setContentText(body) // 본문 설정
            .setAutoCancel(true) // 클릭하면 알림이 자동으로 사라지게 설정
            .setContentIntent(pendingIntent) // 클릭 시 아까 만든 예약 티켓 실행
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 높은 우선순위로 팝업 알림 허용
            .setDefaults(NotificationCompat.DEFAULT_ALL) // 기본 소리, 진동 등 사용

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
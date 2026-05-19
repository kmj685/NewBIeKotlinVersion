package com.newBie.new_bie.features.notification.data.datasource

import android.util.Log
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.notification.data.dto.NotificationsDto
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject

class NotificationsDataSource @Inject constructor(){
    private val _supabase = SupabaseManager.supabase

    // 알림 리스트 fetch (supabase 서버 단에서 auth.uid = receiver_id 로 필터링을 이미 해서 더 필터링 할 필요 없음)
    suspend fun fetchNotificationsList(): List<NotificationsDto> {
        return try {
            _supabase.from("notifications")
                .select(
                    columns = Columns.raw(
                        "id, receiver_id, type, target_id, is_read, created_at, sender_id:users!notifications_sender_id_fkey(*)"
                    )
                )
                .decodeList<NotificationsDto>()
        } catch (e: Exception) {
            Log.e(Constants.TAG, "fetchNotificationsList: ${e.message}")
            emptyList()
        }
    }

    // 알림 개별 읽음
    suspend fun readNotification(id: Int){
        try {
            Log.d("Supabase_Debug", "업데이트 시작")

            _supabase.from("notifications")
                .update(
                    update = {
                        set("is_read", true)
                    }
                ) {
                    filter {
                        eq("id", id)
                    }
                }
            Log.d("Supabase_Debug", "업데이트 성공")

        } catch (e: Exception){
            Log.e("Supabase_Debug", "에러 발생: ${e.message}")
        }
    }

    // 알림 전체 읽음
    suspend fun readAllNotification(receiverId: String){
        try {
            _supabase.from("notifications")
                .update(
                    update = {
                        set("is_read", true)
                    }
                ) {
                    filter {
                        eq("receiver_id", receiverId)
                    }
                }
        } catch (e: Exception){
            Log.e(Constants.TAG, "readNotification: ${e.message}", )
        }
    }

    // 알림 개별 삭제
    suspend fun deletedNotification(id: Int){
        try {
            _supabase.from("notifications")
                .delete {
                    filter {
                        eq("id", id)
                    }
                }
        } catch (e: Exception){
            Log.e(Constants.TAG, "deletedNotification: ${e.message}")
        }
    }

    // 알림 전체 삭제
    suspend fun deletedAllNotification(receiverId: String){
        try {
            _supabase.from("notifications")
                .delete {
                    filter {
                        eq("receiver_id", receiverId)
                    }
                }
        } catch (e: Exception){
            Log.e(Constants.TAG, "deletedNotification: ${e.message}")
        }
    }
}
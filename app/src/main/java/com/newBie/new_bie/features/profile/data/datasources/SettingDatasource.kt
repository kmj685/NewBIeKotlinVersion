package com.newBie.new_bie.features.profile.data.datasources

import android.util.Log
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.data.dto.NotificationStatusDto
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

class SettingDatasource @Inject constructor(){

    private val _supabase = SupabaseManager.supabase

    // 알림 설정-해제 토글 함수
    suspend fun setNotificationEnabled(userId: String, isEnabled: Boolean){
        try {
            _supabase.from("users")
                .update({
                    set("fcm_is_notification_enabled", isEnabled)
                }){
                    filter { eq("id", userId) }
                }
            Log.d(TAG, "setNotificationEnabled: $isEnabled")
        } catch (e: Exception){
            Log.e(TAG, "setNotificationEnabled: ${e.message}", )
        }
    }

    // 알림 설정 여부 확인
    suspend fun getNotificationStatus(userId: String): Boolean {
        return try {
            val result = _supabase.from("users")
                .select(columns = Columns.raw("fcm_is_notification_enabled")){
                    filter {
                        eq("id", userId)
                    }
                }.decodeSingle<NotificationStatusDto>()

            result.fcmIsNotificationEnabled
        } catch (e: Exception){
            Log.e(TAG, "getNotificationStatus: ${e.message}", )
            true
        }
    }
}
package com.newBie.new_bie.features.profile.data.datasources

import android.util.Log
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.data.dto.NoticesDto
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject

class NoticesDatasource @Inject constructor(){

    private val _supabase = SupabaseManager.supabase

    // 공지사항 전체 리스트
    suspend fun getNoticesList(): List<NoticesDto> {
        return try {
            _supabase.from("notices")
                .select(){
                    order(column = "created_at", order = Order.DESCENDING)
                }
                .decodeList<NoticesDto>()
        } catch (e: Exception) {
            Log.e(TAG, "getNoticesList: ${e.message}", )
            emptyList()
        }
    }

    // 공지사항 단일
    suspend fun getNotice(noticeId: Int): NoticesDto? {
        return try {
            _supabase.from("notices")
                .select{
                    filter{
                        eq("id", noticeId)
                    }
                }.decodeSingle()
        } catch (e: Exception) {
            Log.e(TAG, "getNotices: ${e.message}", )
            null
        }
    }
}
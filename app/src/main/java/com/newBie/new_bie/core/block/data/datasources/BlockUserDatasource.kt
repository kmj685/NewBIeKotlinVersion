package com.newBie.new_bie.core.block.data.datasources

import android.util.Log
import com.newBie.new_bie.core.block.data.dto.BlockUserDto
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.core.utils.Constants.TAG
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class BlockUserDatasource @Inject constructor() {
    private val _supabase = SupabaseManager.supabase

    // 유저-유저 차단
    suspend fun insertBlockUser(userId: String, blockUserId: String){
        try {
            _supabase.from("blocked_users")
                .insert(
                    buildJsonObject {
                        put("user_id", userId)
                        put("blocked_user_id", blockUserId)
                    }
                )
        } catch (e: Exception) {
            Log.e(Constants.TAG, "insertBlockUser: ${e.message}", )
        }
    }

    // 유저-유저 차단해제
    suspend fun deleteBlockUser(userId: String, blockUserId: String){
        try {
            _supabase.from("blocked_users")
                .delete {
                    filter {
                        eq("user_id", userId)
                        eq("blocked_user_id", blockUserId)
                    }
                }
        } catch (e: Exception){
            Log.e(Constants.TAG, "deleteBlockUser: ${e.message}", )
        }
    }

    // 유저-유저 차단 리스트
    suspend fun getBlockUserList(userId: String): List<BlockUserDto>{
        return try {
            _supabase.from("blocked_users")
                .select(
                    columns = Columns.raw(
                        "id, user_id, created_at, blocked_user_id(*)"
                    )
                ) {
                    filter {
                        eq("user_id", userId)
                    }
                }.decodeList<BlockUserDto>()
        } catch (e: Exception){
            Log.e(Constants.TAG, "getBlockUserList: ${e.message}", e)
            emptyList()
        }
    }

    // 유저 신고
    suspend fun insertReportUser(senderId: String, reportedId: String, category: String, content: String?){
        try {
            _supabase.from("report")
                .insert(
                    buildJsonObject {
                        put("sender_id", senderId)
                        put("reported_id", reportedId)
                        put("category", category)
                        put("content", content)
                    }
                )
        } catch (e: Exception) {
            Log.e(TAG, "insertReportUser: ${e.message}", )
        }
    }
}
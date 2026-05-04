package com.newBie.new_bie.features.profile.data.datasources

import android.util.Log
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.data.dto.UserDto
import com.newBie.new_bie.features.post.data.mapper.toEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.profile.data.dto.FollowDto
import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ProfileDatasource {

    private val _supabase = SupabaseManager.supabase

    // 팔로워 리스트 fetch
    suspend fun fetchFollowerList(
        userId: String
    ): List<FollowEntity> {
        return try {
            _supabase.from("follow")
                .select(columns = Columns.raw(
                    """
                        id,
                        follower_id,
                        following_id,
                        created_at,
                        users:follower_id(*)
                    """.trimIndent()
                )) {
                    filter {
                        eq("following_id", userId)
                    }
            }.decodeList<FollowDto>().map { it.toEntity() }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "follow list: ${e.message}", )
            emptyList()
        }
    }

    // 팔로잉 리스트 fetch
    suspend fun fetchFollowingList(
        userId: String
    ): List<FollowEntity> {
        return try {
            _supabase.from("follow")
                .select(columns = Columns.raw(
                    """
                        id,
                        follower_id,
                        following_id,
                        created_at,
                        users:following_id(*)
                    """.trimIndent()
                )) {
                    filter {
                        eq("follower_id", userId)
                    }
                }.decodeList<FollowDto>().map { it.toEntity() }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "follow list: ${e.message}", )
            emptyList()
        }
    }

    // 팔로우
    suspend fun followUser(
        myId: String,
        targetUserId: String
    ){
        try {
            _supabase
                .from("follow")
                .insert(
                    buildJsonObject {
                        put("follower_id", myId)
                        put("following_id", targetUserId)
                    }
                )
        } catch (e: Exception){
            Log.e(Constants.TAG, "팔로우 실패: ${e.message}", )
        }

    }

    // 언팔로우
    suspend fun unfollowUser(
        myId: String,
        targetUserId: String
    ) {
        try {
            _supabase
                .from("follow")
                .delete {
                    filter{
                        eq("follower_id", myId)
                        eq("following_id", targetUserId)
                    }
                }
        } catch (e: Exception){
            Log.e(Constants.TAG, "언팔로우 실패: ${e.message}", )
        }

    }

    // 팔로잉 하고 있는지 체크
    suspend fun isFollowing(
        myId: String,
        targetUserId: String
    ): Boolean {
        return try {
            val result = _supabase
                .from("follow")
                .select {
                    filter {
                        eq("follower_id", myId) // 내가(Follower)
                        eq("following_id", targetUserId) // 상대방을(Following)
                    }
                }
            // 결과 리스트가 비어있지(공백) 않다면(팔로우 중이라면) true 반환
            // DTO로 변환하지 말고, 데이터가 존재하는지만 확인
            result.data != "[]"

        } catch (e: Exception){
            Log.d(Constants.TAG, "isFollowing 체크 실패: ${e.message}")
            false
        }
    }

    suspend fun fetchUser(
        targetUserId: String
    ): UserEntity? {
        return try {
            _supabase.from("users").select {
                filter {
                    eq("id", targetUserId)
                }
            }.decodeSingle<UserDto>().toEntity()
        } catch (e: Exception) {
            Log.e(Constants.TAG, "fetchUser: ${e.message}", )
            null
        }
    }
}
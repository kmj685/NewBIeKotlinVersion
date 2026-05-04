package com.newBie.new_bie.features.profile.data.dto

import com.newBie.new_bie.features.post.data.dto.UserDto
import com.newBie.new_bie.features.post.data.mapper.toEntity
import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FollowDto(
    val id: Int,
    @SerialName("follower_id") val followerId: String,
    @SerialName("following_id") val followingId: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("users") val users: UserDto // 이 부분이 UserDto에서 가져오는 부분이다.
){
    fun toEntity(isFollowing: Boolean = false): FollowEntity{
        return FollowEntity(
            id = id,
            followerId = followerId,
            followingId = followingId,
            createdAt = createdAt,
            users = users.toEntity(),
            isFollowing = isFollowing
        )
    }
}
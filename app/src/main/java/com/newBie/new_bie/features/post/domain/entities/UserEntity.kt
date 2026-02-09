package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: String,
    @SerialName("profile_image") val profileImage: String?,
    @SerialName("nick_name") val nickName: String?,
    val introduction: String?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("unregister_at") val unregisterAt: String?,
    @SerialName("following_count") val followingCount: Int,
    @SerialName("follower_count") val followerCount: Int,
    val email: String?,
    @SerialName("is_blocked") val isBlocked: Boolean
)
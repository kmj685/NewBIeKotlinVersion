package com.newBie.new_bie.features.post.data.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class UserDto(
    val id: String,
    @SerializedName("profile_image") val profileImage: String?,
    @SerializedName("nick_name") val nickName: String?,
    val introduction: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("unregister_at") val unregisterAt: String?,
    @SerializedName("following_count") val followingCount: Int,
    @SerializedName("follower_count") val followerCount: Int,
    val email: String?,
    @SerializedName("is_blocked") val isBlocked: Boolean
)
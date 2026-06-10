package com.newBie.new_bie.features.post.data.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
    val id: String,
    @SerializedName("profile_image") @SerialName("profile_image") val profileImage: String?,
    @SerializedName("nick_name") @SerialName("nick_name") val nickName: String?,
    val introduction: String?,
    @SerializedName("created_at") @SerialName("created_at") val createdAt: String,
    @SerializedName("unregister_at") @SerialName("unregister_at") val unregisterAt: String?,
    @SerializedName("post_count") @SerialName("post_count") val postCount: Int,
    @SerializedName("following_count") @SerialName("following_count") val followingCount: Int,
    @SerializedName("follower_count") @SerialName("follower_count") val followerCount: Int,
    val email: String?,
    @SerializedName("is_blocked") @SerialName("is_blocked") val isBlocked: Boolean
)
package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class UserEntity(
    val id: String,
    val profileImage: String?,
    val nickName: String?,
    val introduction: String?,
    val createdAt: String?,
    val unregisterAt: String?,
    val followerCount: Int,
    val followingCount: Int,
    val email: String?,
    val isBlocked: Boolean
)
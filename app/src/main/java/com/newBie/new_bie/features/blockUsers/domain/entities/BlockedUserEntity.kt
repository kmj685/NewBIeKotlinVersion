package com.newBie.new_bie.features.blockUsers.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockedUserEntity(
    val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("blocked_user_id") val blockedUserId: String,
    @SerialName("created_at") val createdAt: String
)
package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikesEntity(
    val id: Int,
    @SerialName("post_id") val postId: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("created_at") val createdAt: String
)
package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostEntity(
    val id: Int,
    @SerialName("author_id") val authorId: String,
    val title: String?,
    val content: String?,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("deleted_at") val deletedAt: String?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_block") val isBlock: Boolean,
    @SerialName("likes_count") val likesCount: Int,
    @SerialName("comments_count") val commentsCount: Int
)
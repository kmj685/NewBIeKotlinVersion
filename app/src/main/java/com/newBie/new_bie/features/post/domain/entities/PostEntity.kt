package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class PostEntity(
    val id: Int,
    val authorId: String,
    val title: String?,
    val content: String?,
    val imageUrl: String?,
    val updatedAt: String?,
    val deletedAt: String?,
    val createdAt: String,
    val isBlock: Boolean,
    val likesCount: Int,
    val commentsCount: Int
)
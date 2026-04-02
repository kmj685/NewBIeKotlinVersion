package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class CommentsEntity(
    val id: Int,
    val postId: Int,
    val authorId: String,
    val content: String,
    val createdAt: String
)
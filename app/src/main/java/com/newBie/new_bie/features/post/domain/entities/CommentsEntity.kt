package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentsEntity(
    val id: Int,
    @SerialName("post_id") val postId: Int,
    @SerialName("author_id") val authorId: String,
    val content: String,
    @SerialName("created_at") val createdAt: String
)
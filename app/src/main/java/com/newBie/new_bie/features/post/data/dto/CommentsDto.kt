package com.newBie.new_bie.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentsDto(
    val id: Int,
    @SerialName("post_id") val postId: Int,
    @SerialName("author_id") val authorId: String,
    val content: String,
    @SerialName("created_at") val createdAt: String
)
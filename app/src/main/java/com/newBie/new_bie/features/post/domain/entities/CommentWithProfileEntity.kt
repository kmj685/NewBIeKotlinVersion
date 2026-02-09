package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentWithProfileEntity(
    val id: Int,
    @SerialName("post_id") val postId: Int,
    @SerialName("author_id") val authorId: String?,
    var content: String?,
    @SerialName("deleted_at") val deletedAt: String?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_block") val isBlock: Boolean,
    @SerialName("users") val user: PostUserEntity
)
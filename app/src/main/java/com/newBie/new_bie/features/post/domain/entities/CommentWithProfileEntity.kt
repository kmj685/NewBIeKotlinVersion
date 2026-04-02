package com.newBie.new_bie.features.post.domain.entities

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class CommentWithProfileEntity(
    val id: Int,
    val postId: Int,
    val authorId: String?,
    var content: String?,
    val deletedAt: String?,
    val createdAt: String,
    val isBlock: Boolean,
    val user: PostUserEntity
)
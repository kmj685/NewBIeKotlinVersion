package com.newBie.new_bie.features.post.domain.entities

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class CommentWithProfileEntity(
    val id: Int,
    @SerializedName("post_id") val postId: Int,
    @SerializedName("author_id") val authorId: String?,
    var content: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_block") val isBlock: Boolean,
    @SerializedName("users") val user: PostUserEntity
)
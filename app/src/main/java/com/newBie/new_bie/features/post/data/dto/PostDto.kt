package com.newBie.new_bie.features.post.data.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class PostDto(
    val id: Int,
    @SerializedName("author_id") val authorId: String,
    val title: String?,
    val content: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_block") val isBlock: Boolean,
    @SerializedName("likes_count") val likesCount: Int,
    @SerializedName("comments_count") val commentsCount: Int
)
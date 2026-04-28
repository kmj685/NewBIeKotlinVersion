package com.newBie.new_bie.features.post.data.dto

import com.google.gson.annotations.SerializedName
data class CommentWithProfileDto(
    val id: Int,
    @SerializedName("post_id") val postId: Int,
    @SerializedName("author_id") val authorId: String?,
    var content: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_block") val isBlock: Boolean,
    @SerializedName("users") val user: PostUserDto
)
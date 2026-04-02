package com.newBie.new_bie.features.post.data.dto

import com.google.gson.annotations.SerializedName
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity

data class PostWithProfileDto(
    val id: Int,
    val title: String?,
    val content: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_block") val isBlock: Boolean,

    @SerializedName("likes_count") var likesCount: Int,
    @SerializedName("comments_count") var commentsCount: Int,
    @SerializedName("is_liked") var isLiked: Boolean,

    @SerializedName("users") val user: PostUserDto?,
    @SerializedName("post_images") val postImages: List<PostImageDto> = emptyList(),
    @SerializedName("category") val categories: List<CategoryDto> = emptyList()
)


data class PostUserDto(
    val id: String,
    @SerializedName("nick_name") val nickName: String?,
    @SerializedName("profile_image") val profileImage: String?
)


data class PostImageDto(
    val id: Int,
    val index: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("created_at") val createdAt: String
)

data class CategoryDto(
    val id: Int,
    @SerializedName("category_type") val categoryType: CategoryTypeDto
)

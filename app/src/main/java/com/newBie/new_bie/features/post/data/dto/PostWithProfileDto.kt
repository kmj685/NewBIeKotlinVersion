package com.newBie.new_bie.features.post.data.dto

import com.google.gson.annotations.SerializedName
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostWithProfileDto(
    val id: Int,
    val title: String?,
    val content: String?,
    @SerializedName("image_url") @SerialName("image_url") val imageUrl: String?,
    @SerializedName("updated_at") @SerialName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") @SerialName("deleted_at") val deletedAt: String?,
    @SerializedName("created_at") @SerialName("created_at") val createdAt: String,
    @SerializedName("is_block") @SerialName("is_block") val isBlock: Boolean,

    @SerializedName("likes_count") @SerialName("likes_count") var likesCount: Int,
    @SerializedName("comments_count") @SerialName("comments_count") var commentsCount: Int,
    @SerializedName("is_liked") @SerialName("is_liked") var isLiked: Boolean = false,

    @SerializedName("users") @SerialName("users") val user: PostUserDto? = null,
    @SerializedName("post_images") @SerialName("post_images") val postImages: List<PostImageDto> = emptyList(),
    @SerializedName("category") @SerialName("category") val categories: List<CategoryDto> = emptyList()
)

@Serializable
data class PostUserDto(
    val id: String,
    @SerializedName("nick_name") @SerialName("nick_name") val nickName: String?,
    @SerializedName("profile_image") @SerialName("profile_image") val profileImage: String?
)

@Serializable
data class PostImageDto(
    val id: Int,
    val index: Int,
    @SerializedName("image_url") @SerialName("image_url") val imageUrl: String,
    @SerializedName("created_at") @SerialName("created_at") val createdAt: String
)
@Serializable
data class CategoryDto(
    val id: Int,
    @SerializedName("category_type") @SerialName("category_type") val categoryType: CategoryTypeDto
)

package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostWithProfileEntity(
    val id: Int,
    val title: String?,
    val content: String?,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("deleted_at") val deletedAt: String?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_block") val isBlock: Boolean,

    var likesCount: Int,
    var commentsCount: Int,
    @SerialName("is_liked") var isLiked: Boolean?,

    @SerialName("users") val user: PostUserEntity,
    @SerialName("post_images") val postImages: List<PostImageEntity> = emptyList(),
    @SerialName("category") val categories: List<CategoryEntity> = emptyList()
)

@Serializable
data class PostUserEntity(
    val id: String,
    @SerialName("nick_name") val nickName: String?,
    @SerialName("profile_image") val profileImage: String?
)

@Serializable
data class PostImageEntity(
    val id: Int,
    val index: Int,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class CategoryEntity(
    val id: Int,
    @SerialName("category_type") val categoryType: CategoryTypeEntity
)



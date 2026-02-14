package com.newBie.new_bie.features.post.domain.entities

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class PostWithProfileEntity(
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

    @SerializedName("users") val user: PostUserEntity?,
    @SerializedName("post_images") val postImages: List<PostImageEntity> = emptyList(),
    @SerializedName("category") val categories: List<CategoryEntity> = emptyList()
)


data class PostUserEntity(
    val id: String,
    @SerializedName("nick_name") val nickName: String?,
    @SerializedName("profile_image") val profileImage: String?
)


data class PostImageEntity(
    val id: Int,
    val index: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("created_at") val createdAt: String
)

data class CategoryEntity(
    val id: Int,
    @SerializedName("category_type") val categoryType: CategoryTypeEntity
)



package com.newBie.new_bie.features.post.domain.entities

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class PostWithProfileEntity(
    val id: Int,
    val title: String?,
    val content: String?,
    val imageUrl: String?,
    val updatedAt: String?,
    val deletedAt: String?,
    val createdAt: String,
    val isBlock: Boolean,

    var likesCount: Int,
    var commentsCount: Int,
    var isLiked: Boolean,

    val user: PostUserEntity?,
    val postImages: List<PostImageEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList()
)


data class PostUserEntity(
    val id: String,
    val nickName: String?,
    val profileImage: String?
)


data class PostImageEntity(
    val id: Int,
    val index: Int,
    val imageUrl: String,
    val createdAt: String
)

data class CategoryEntity(
    val id: Int,
    val categoryType: CategoryTypeEntity
)



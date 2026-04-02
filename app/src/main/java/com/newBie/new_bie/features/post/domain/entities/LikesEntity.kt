package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class LikesEntity(
    val id: Int,
    val postId: Int,
    val userId: String,
    val createdAt: String
)
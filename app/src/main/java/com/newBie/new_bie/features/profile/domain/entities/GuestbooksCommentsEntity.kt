package com.newBie.new_bie.features.profile.domain.entities

import com.newBie.new_bie.features.post.domain.entities.UserEntity

data class GuestbooksCommentsEntity(
    val id: Int,
    val guestbooksId: Int,
    val authorId: UserEntity,
    val content: String,
    val createdAt: String,
    val deletedAt: String?,
    val isBlock: Boolean
)

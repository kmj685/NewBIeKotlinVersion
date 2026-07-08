package com.newBie.new_bie.core.block.domain.entities

import com.newBie.new_bie.features.post.domain.entities.UserEntity

data class BlockUserEntity(
    val id: Int,
    val userId: String,
    val blockedUserId: UserEntity,
    val createdAt: String
)
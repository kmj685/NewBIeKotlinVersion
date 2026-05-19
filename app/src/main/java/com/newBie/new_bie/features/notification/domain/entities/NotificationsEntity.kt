package com.newBie.new_bie.features.notification.domain.entities

import com.newBie.new_bie.features.post.domain.entities.UserEntity

data class NotificationsEntity(
    val id: Int,
    val receiverId: String,
    val senderId: UserEntity,
    val type: String,
    val targetId: String,
    val isRead: Boolean,
    val createdAt: String
)

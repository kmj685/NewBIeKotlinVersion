package com.newBie.new_bie.features.notification.data.dto

import com.newBie.new_bie.features.notification.domain.entities.NotificationsEntity
import com.newBie.new_bie.features.post.data.dto.UserDto
import com.newBie.new_bie.features.post.data.mapper.toEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationsDto(
    val id: Int,
    @SerialName("receiver_id") val receiverId: String,
    val type: String,
    @SerialName("target_id") val targetId: String,
    @SerialName("is_read") val isRead: Boolean,
    @SerialName("created_at") val createdAt: String,

    @SerialName("sender_id") val senderId: UserDto
){

    // 순수한 코틀린 도메인인 Entity와 연결해주는 다리 역할
    fun toEntity(): NotificationsEntity {
        return NotificationsEntity(
            id = id,
            receiverId = receiverId,
            senderId = senderId.toEntity(),
            type = type,
            targetId = targetId,
            isRead = isRead,
            createdAt = createdAt
        )
    }
}
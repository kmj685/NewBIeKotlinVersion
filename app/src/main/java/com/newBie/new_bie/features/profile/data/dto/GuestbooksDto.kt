package com.newBie.new_bie.features.profile.data.dto

import com.newBie.new_bie.features.post.data.dto.UserDto
import com.newBie.new_bie.features.post.data.mapper.toEntity
import com.newBie.new_bie.features.profile.domain.entities.GuestbooksEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuestbooksDto(
    val id: Int,
    @SerialName("receiver_id") val receiverId: UserDto,
    @SerialName("sender_id") val senderId: UserDto,
    val title: String,
    val content: String,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("deleted_at") val deletedAt: String?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("comments_count") val commentsCount: Int
){
    fun toEntity(): GuestbooksEntity{
        return GuestbooksEntity(
            id = id,
            receiverId = receiverId.toEntity(),
            senderId = senderId.toEntity(),
            title = title,
            content = content,
            imageUrl = imageUrl,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
            createdAt = createdAt,
            commentsCount = commentsCount
        )
    }
}

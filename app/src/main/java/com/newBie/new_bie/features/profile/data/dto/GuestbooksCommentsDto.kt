package com.newBie.new_bie.features.profile.data.dto

import com.newBie.new_bie.features.post.data.dto.UserDto
import com.newBie.new_bie.features.post.data.mapper.toEntity
import com.newBie.new_bie.features.profile.domain.entities.GuestbooksCommentsEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuestbooksCommentsDto(
    val id: Int,
    @SerialName("guestbooks_id") val guestbooksId: Int,
    @SerialName("author_id") val authorId: UserDto,
    val content: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("deleted_at") val deletedAt: String?,
    @SerialName("is_block") val isBlock: Boolean
){
    fun toEntity(): GuestbooksCommentsEntity{
        return GuestbooksCommentsEntity(
            id = id,
            guestbooksId = guestbooksId,
            authorId = authorId.toEntity(),
            content = content,
            createdAt = createdAt,
            deletedAt = deletedAt,
            isBlock = isBlock
        )
    }
}

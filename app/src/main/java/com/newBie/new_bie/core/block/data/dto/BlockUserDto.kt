package com.newBie.new_bie.core.block.data.dto

import com.newBie.new_bie.core.block.domain.entities.BlockUserEntity
import com.newBie.new_bie.features.post.data.dto.UserDto
import com.newBie.new_bie.features.post.data.mapper.toEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockUserDto(
    val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("blocked_user_id") val blockedUserId: UserDto,
    @SerialName("created_at") val createdAt: String
){
    fun toEntity(): BlockUserEntity {
        return BlockUserEntity(
            id = id,
            userId = userId,
            blockedUserId = blockedUserId.toEntity(),
            createdAt = createdAt
        )
    }
}
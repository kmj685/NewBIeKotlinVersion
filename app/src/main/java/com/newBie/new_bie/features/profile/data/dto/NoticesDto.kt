package com.newBie.new_bie.features.profile.data.dto

import com.newBie.new_bie.features.profile.domain.entities.NoticesEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticesDto(
    val id: Int,
    val title: String,
    val content: String,
    @SerialName("created_at") val createdAt: String
){
    fun toEntity(): NoticesEntity{
        return NoticesEntity(
            id = id,
            title = title,
            content = content,
            createdAt = createdAt
        )
    }
}

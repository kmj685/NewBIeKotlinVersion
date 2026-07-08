package com.newBie.new_bie.core.block.data.dto

import com.newBie.new_bie.core.block.domain.entities.ReportUserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportUserDto(
    val id: Int,
    @SerialName("sender_id") val senderId: String,
    @SerialName("reported_id") val reportedId: String,
    val category: String,
    val content: String?,
    @SerialName("created_at") val createdAt: String
){
    fun toEntity(): ReportUserEntity{
        return ReportUserEntity(
            id = id,
            senderId = senderId,
            reportedId = reportedId,
            category = category,
            content = content,
            createdAt = createdAt
        )
    }
}

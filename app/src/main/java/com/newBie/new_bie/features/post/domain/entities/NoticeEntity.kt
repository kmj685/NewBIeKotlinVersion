package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeEntity(
    val id: Int,
    val title: String?,
    val content: String?,
    @SerialName("published_at") val publishedAt: String?,
    @SerialName("created_at") val createdAt: String
)
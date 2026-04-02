package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class NoticeEntity(
    val id: Int,
    val title: String?,
    val content: String?,
    val publishedAt: String?,
    val createdAt: String
)
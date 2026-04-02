package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class ReportPostEntity(
    val id: Int,
    val postId: Int,
    val reporterId: String,
    val reason: String,
    val createdAt: String,
    val reviewedAt: String?
)
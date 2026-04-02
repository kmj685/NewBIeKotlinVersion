package com.newBie.new_bie.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportPostDto(
    val id: Int,
    @SerialName("post_id") val postId: Int,
    @SerialName("reporter_id") val reporterId: String,
    val reason: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("reviewed_at") val reviewedAt: String?
)
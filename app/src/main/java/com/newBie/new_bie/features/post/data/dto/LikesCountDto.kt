package com.newBie.new_bie.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikesCountDto(
    @SerialName("likes_count") val likesCount: Int
)
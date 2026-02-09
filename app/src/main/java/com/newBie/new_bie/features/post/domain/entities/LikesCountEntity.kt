package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikesCountEntity(
    @SerialName("likes_count") val likesCount: Int
)
package com.newBie.new_bie.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActionResponse(
    val message: String,
    @SerialName("post_id") val postId: Int
)
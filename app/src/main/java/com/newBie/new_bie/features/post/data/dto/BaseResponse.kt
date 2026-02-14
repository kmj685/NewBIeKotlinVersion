package com.newBie.new_bie.features.post.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val data: T? = null,
    val message: String
)

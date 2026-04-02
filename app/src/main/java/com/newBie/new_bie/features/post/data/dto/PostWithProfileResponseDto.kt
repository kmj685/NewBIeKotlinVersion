package com.newBie.new_bie.features.post.data.dto

import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity

data class PostWithProfileResponseDto(
    val data: PostWithProfileDto,
    val message: String
)

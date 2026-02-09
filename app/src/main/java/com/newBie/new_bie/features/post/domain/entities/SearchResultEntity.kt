package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultEntity(
    val keyword: String,
    val type: String,
    val posts: List<PostWithProfileEntity> = emptyList(),
    val users: List<UserEntity> = emptyList(),
    @SerialName("total_count") val totalCount: Int
)
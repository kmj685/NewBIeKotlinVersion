package com.newBie.new_bie.features.post.domain.entities

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class SearchResultEntity(
    val keyword: String,
    val type: String,
    val posts: List<PostWithProfileEntity> = emptyList(),
    val users: List<UserEntity> = emptyList(),
    @SerializedName("total_count") val totalCount: Int
)
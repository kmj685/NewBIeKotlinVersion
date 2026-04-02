package com.newBie.new_bie.features.post.data.dto

import com.google.gson.annotations.SerializedName


data class SearchResultDto(
    val keyword: String,
    val type: String,
    val posts: List<PostWithProfileDto> = emptyList(),
    val users: List<UserDto> = emptyList(),
    @SerializedName("total_count") val totalCount: Int
)
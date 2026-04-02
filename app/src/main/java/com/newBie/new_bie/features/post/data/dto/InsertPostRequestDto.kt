package com.newBie.new_bie.features.post.data.dto

data class InsertPostRequestDto(
    val author_id: String,
    val title: String,
    val content: String,
    val images: List<String>,
    val categories: List<Int>
)

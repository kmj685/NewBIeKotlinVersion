package com.newBie.new_bie.features.post.data.dto

data class UpdatePostDto(
    val title: String,
    val content: String,
    val images: List<String>,
    val categories: List<Int>
)

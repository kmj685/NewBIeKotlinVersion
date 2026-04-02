package com.newBie.new_bie.features.post.data.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



data class CategoryTypeDto(
    val id: Int,
    @SerializedName("type_title")
    val typeTitle: String
)

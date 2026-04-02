package com.newBie.new_bie.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryTypeTitleDto (
    @SerialName("type_title")
    val typeTitle: String
)
package com.newBie.new_bie.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryTypeDtoWithSupabase(
    val id: Int,
    @SerialName("type_title") val typeTitle: String
)
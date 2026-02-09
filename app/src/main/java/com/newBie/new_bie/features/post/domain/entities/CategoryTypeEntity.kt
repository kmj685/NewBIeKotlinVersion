package com.newBie.new_bie.features.post.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryTypeEntity(
    val id: Int,
    @SerialName("type_title") val typeTitle: String
)
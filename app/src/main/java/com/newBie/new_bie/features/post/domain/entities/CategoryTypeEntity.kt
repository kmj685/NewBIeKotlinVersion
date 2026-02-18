package com.newBie.new_bie.features.post.domain.entities

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class CategoryTypeEntity(
    val id: Int,
    @SerializedName("type_title") val typeTitle: String
)
@Serializable
data class CategoryTypeEntityWithSupabase(
    val id: Int,
    @SerialName("type_title") val typeTitle: String
)
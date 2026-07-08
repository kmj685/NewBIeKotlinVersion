package com.newBie.new_bie.core.block.domain.entities

data class ReportUserEntity(
    val id: Int,
    val senderId: String,
    val reportedId: String,
    val category: String,
    val content: String?,
    val createdAt: String
)
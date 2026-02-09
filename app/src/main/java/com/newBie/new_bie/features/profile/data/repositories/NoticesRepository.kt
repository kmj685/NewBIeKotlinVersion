package com.newBie.new_bie.features.profile.data.repositories

import com.newBie.new_bie.features.post.domain.entities.NoticeEntity
import com.newBie.new_bie.features.profile.domain.repositories.NoticesRepository

class NoticesRepositoryImpl : NoticesRepository {
    override suspend fun fetchNotices(): List<NoticeEntity> {
        return emptyList()
    }

    override suspend fun getById(id: Int): NoticeEntity? {
        return null
    }
}
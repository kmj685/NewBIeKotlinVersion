package com.newBie.new_bie.features.profile.domain.repositories

import com.newBie.new_bie.features.post.domain.entities.NoticeEntity

interface NoticesRepository {
    suspend fun fetchNotices() : List<NoticeEntity>

    suspend fun getById(id : Int) : NoticeEntity?
}
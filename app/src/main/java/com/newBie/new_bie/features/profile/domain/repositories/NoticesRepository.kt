package com.newBie.new_bie.features.profile.domain.repositories

import com.newBie.new_bie.features.profile.domain.entities.NoticesEntity

interface NoticesRepository {
    suspend fun getNoticesList(): List<NoticesEntity>
    suspend fun getNotice(noticeId: Int): NoticesEntity?
}
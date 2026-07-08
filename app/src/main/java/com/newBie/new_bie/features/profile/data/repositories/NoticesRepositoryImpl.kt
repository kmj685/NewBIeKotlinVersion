package com.newBie.new_bie.features.profile.data.repositories

import com.newBie.new_bie.features.profile.data.datasources.NoticesDatasource
import com.newBie.new_bie.features.profile.domain.entities.NoticesEntity
import com.newBie.new_bie.features.profile.domain.repositories.NoticesRepository
import javax.inject.Inject

class NoticesRepositoryImpl @Inject constructor(private val datasource: NoticesDatasource): NoticesRepository{
    override suspend fun getNoticesList(): List<NoticesEntity> {
        val result = datasource.getNoticesList()

        return result.map { it.toEntity() }
    }

    override suspend fun getNotice(noticeId: Int): NoticesEntity? {
        val result = datasource.getNotice(noticeId = noticeId)

        return result?.toEntity()
    }
}
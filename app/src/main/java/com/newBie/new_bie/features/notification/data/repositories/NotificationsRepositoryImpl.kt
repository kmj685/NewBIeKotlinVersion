package com.newBie.new_bie.features.notification.data.repositories

import com.newBie.new_bie.features.notification.data.datasource.NotificationsDataSource
import com.newBie.new_bie.features.notification.domain.entities.NotificationsEntity
import com.newBie.new_bie.features.notification.domain.repository.NotificationsRepository
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(private val dataSource: NotificationsDataSource):
    NotificationsRepository {
    override suspend fun fetchNotifications(): List<NotificationsEntity> {
        // DataSource에서 Dto 리스트를 가져온다
        val dtoList = dataSource.fetchNotificationsList()
        // dto들을 각각 Entity로 변환해서 반환한다.
        return dtoList.map { it.toEntity() }
    }

    override suspend fun readNotification(id: Int) {
        dataSource.readNotification(id)
    }

    override suspend fun readAllNotification(receiverId: String) {
        dataSource.readAllNotification(receiverId)
    }

    override suspend fun deletedNotification(id: Int) {
        dataSource.deletedNotification(id)
    }

    override suspend fun deletedAllNotification(receiverId: String) {
        dataSource.deletedAllNotification(receiverId)
    }
}
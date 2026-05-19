package com.newBie.new_bie.features.notification.domain.repository

import com.newBie.new_bie.features.notification.domain.entities.NotificationsEntity

interface NotificationsRepository {

    // 알림 리스트 fetch
    suspend fun fetchNotifications(): List<NotificationsEntity>
    // 알림 읽음
    suspend fun readNotification(id: Int)
    // 알림 전체 읽음
    suspend fun readAllNotification(receiverId: String)
    // 알림 삭제
    suspend fun deletedNotification(id: Int)
    // 알림 전체 삭제
    suspend fun deletedAllNotification(receiverId: String)
}
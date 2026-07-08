package com.newBie.new_bie.features.profile.domain.repositories

interface SettingRepository {
    suspend fun setNotificationEnabled(userId: String, isEnabled: Boolean)
    suspend fun getNotificationStatus(userId: String): Boolean
}
package com.newBie.new_bie.features.profile.data.repositories

import com.newBie.new_bie.features.profile.data.datasources.SettingDatasource
import com.newBie.new_bie.features.profile.domain.repositories.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(private val datasource: SettingDatasource): SettingRepository{
    override suspend fun setNotificationEnabled(userId: String, isEnabled: Boolean) {
        datasource.setNotificationEnabled(
            userId = userId,
            isEnabled = isEnabled
        )
    }

    override suspend fun getNotificationStatus(userId: String): Boolean {
        return datasource.getNotificationStatus(userId)
    }
}
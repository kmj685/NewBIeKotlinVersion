package com.newBie.new_bie.features.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationStatusDto(
    @SerialName("fcm_is_notification_enabled") val fcmIsNotificationEnabled: Boolean
)

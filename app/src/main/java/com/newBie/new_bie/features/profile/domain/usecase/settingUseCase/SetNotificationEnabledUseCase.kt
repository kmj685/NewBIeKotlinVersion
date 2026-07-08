package com.newBie.new_bie.features.profile.domain.usecase.settingUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.repositories.SettingRepository
import javax.inject.Inject

class SetNotificationEnabledUseCase @Inject constructor(private val repository: SettingRepository){
    suspend operator fun invoke(userId: String, isEnabled: Boolean): Result<Unit>{
        return try {
            val result = repository.setNotificationEnabled(
                userId = userId,
                isEnabled = isEnabled
            )
            Result.success(result)
        } catch (e: Exception){
            Log.e(TAG, "SetNotificationEnabledUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
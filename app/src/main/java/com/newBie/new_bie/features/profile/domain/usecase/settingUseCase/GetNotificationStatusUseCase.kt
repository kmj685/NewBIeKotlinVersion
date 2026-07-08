package com.newBie.new_bie.features.profile.domain.usecase.settingUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.repositories.SettingRepository
import javax.inject.Inject

class GetNotificationStatusUseCase @Inject constructor(private val repository: SettingRepository) {
    suspend operator fun invoke(userId: String): Result<Boolean> {
        return try {
            val result = repository.getNotificationStatus(userId = userId)
            Result.success(result)
        } catch (e: Exception){
            Log.e(TAG, "GetNotificationStatus: ${e.message}", )
            Result.failure(e)
        }
    }
}
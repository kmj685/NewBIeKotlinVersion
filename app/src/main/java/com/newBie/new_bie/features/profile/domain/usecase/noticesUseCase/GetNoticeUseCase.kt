package com.newBie.new_bie.features.profile.domain.usecase.noticesUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.entities.NoticesEntity
import com.newBie.new_bie.features.profile.domain.repositories.NoticesRepository
import javax.inject.Inject

class GetNoticeUseCase @Inject constructor(private val repository: NoticesRepository) {
    suspend operator fun invoke(noticeId :Int): Result<NoticesEntity?> {
        return try {
            val result = repository.getNotice(noticeId = noticeId)

            Result.success(result)
        } catch (e: Exception) {
            Log.e(TAG, "GetNoticeUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
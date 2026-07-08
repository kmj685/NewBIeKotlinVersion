package com.newBie.new_bie.features.profile.domain.usecase.noticesUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.entities.NoticesEntity
import com.newBie.new_bie.features.profile.domain.repositories.NoticesRepository
import javax.inject.Inject

class GetNoticesListUseCase @Inject constructor(private val repository: NoticesRepository) {
    suspend operator fun invoke(): Result<List<NoticesEntity>>{
        return try {
            val result = repository.getNoticesList()
            Result.success(result)
        } catch (e: Exception){
            Log.e(TAG, "GetNoticesListUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
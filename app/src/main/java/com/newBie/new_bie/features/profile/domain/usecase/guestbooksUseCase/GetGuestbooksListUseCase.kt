package com.newBie.new_bie.features.profile.domain.usecase.guestbooksUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.profile.domain.entities.GuestbooksEntity
import com.newBie.new_bie.features.profile.domain.repositories.GuestbooksRepository
import javax.inject.Inject

class GetGuestbooksListUseCase @Inject constructor(private val repository: GuestbooksRepository) {
    suspend operator fun invoke(
        receiverId: String,
        currentIndex: Int,
        perPage: Int): Result<List<GuestbooksEntity>>{
        return try {
            val results = repository.getGuestbooksList(
                receiverId = receiverId,
                currentIndex = currentIndex,
                perPage = perPage
            )
            Log.d(Constants.TAG, "Guestbooks UseCase: $results")
            Result.success(results)
        } catch (e: Exception){
            Log.e(Constants.TAG, "Guestbooks UseCase 에러: ${e.message}", )
            Result.failure(e)
        }
    }
}
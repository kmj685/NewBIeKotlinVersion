package com.newBie.new_bie.features.profile.domain.usecase.guestbooksCommentsUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.entities.GuestbooksCommentsEntity
import com.newBie.new_bie.features.profile.domain.repositories.GuestbooksCommentsRepository
import javax.inject.Inject

class GetGuestbooksCommentsListUseCase @Inject constructor(private val repository: GuestbooksCommentsRepository) {
    suspend operator fun invoke(guestbookId: Int): Result<List<GuestbooksCommentsEntity>>{
        return try {
            val result = repository.getGuestbooksCommentsList(
                guestbookId = guestbookId
            )
            Result.success(result)
        } catch (e: Exception){
            Log.e(TAG, "GetGuestbooksCommentsListUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
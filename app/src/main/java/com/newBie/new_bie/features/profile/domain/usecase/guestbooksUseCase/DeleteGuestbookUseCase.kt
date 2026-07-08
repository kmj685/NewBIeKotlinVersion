package com.newBie.new_bie.features.profile.domain.usecase.guestbooksUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.domain.repositories.GuestbooksRepository
import javax.inject.Inject

class DeleteGuestbookUseCase @Inject constructor(private val repository: GuestbooksRepository) {
    suspend operator fun invoke(guestbookId: Int): Result<Unit>{
        return try {
            val result = repository.deleteGuestbook(
                guestbookId = guestbookId)
            Result.success(result)
        } catch (e: Exception){
            Log.e(TAG, "DeleteGuestbookUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
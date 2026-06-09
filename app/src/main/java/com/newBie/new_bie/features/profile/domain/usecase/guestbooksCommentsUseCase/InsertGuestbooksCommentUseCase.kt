package com.newBie.new_bie.features.profile.domain.usecase.guestbooksCommentsUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.profile.domain.repositories.GuestbooksCommentsRepository
import javax.inject.Inject

class InsertGuestbooksCommentUseCase @Inject constructor(private val repository: GuestbooksCommentsRepository) {
    suspend operator fun invoke(
        guestbookId: Int,
        userId: String,
        content: String
    ): Result<Unit>{
        return try {
            val result = repository.insertGuestbooksComment(
                guestbookId = guestbookId,
                userId = userId,
                content = content
            )
            Result.success(result)
        } catch (e: Exception){
            Log.e(Constants.TAG, "InsertGuestbooksCommentUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
package com.newBie.new_bie.features.profile.domain.usecase.guestbooksCommentsUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.profile.domain.repositories.GuestbooksCommentsRepository
import javax.inject.Inject

class DeletedGuestbooksCommentUseCase @Inject constructor(private val repository: GuestbooksCommentsRepository) {
    suspend operator fun invoke(commentId: Int): Result<Unit>{
        return try {
            val result = repository.deletedGuestbooksComment(
                commentId = commentId
            )
            Result.success(result)
        } catch (e: Exception){
            Log.e(Constants.TAG, "DeletedGuestbooksCommentUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
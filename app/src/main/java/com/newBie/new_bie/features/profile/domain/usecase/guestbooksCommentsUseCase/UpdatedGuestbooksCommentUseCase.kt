package com.newBie.new_bie.features.profile.domain.usecase.guestbooksCommentsUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.profile.domain.repositories.GuestbooksCommentsRepository
import javax.inject.Inject

class UpdatedGuestbooksCommentUseCase @Inject constructor(private val repository: GuestbooksCommentsRepository) {
    suspend operator fun invoke(
        commentId: Int,
        content: String
    ): Result<Unit>{
        return try {
            val result = repository.updatedGuestbooksComment(
                commentId = commentId,
                content = content
            )
            Result.success(result)
        } catch (e: Exception){
            Log.e(Constants.TAG, "UpdatedGuestbooksCommentUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
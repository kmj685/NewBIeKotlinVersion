package com.newBie.new_bie.features.profile.domain.usecase.guestbooksUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.profile.domain.repositories.GuestbooksRepository
import javax.inject.Inject

class InsertGuestbookUseCase @Inject constructor(private val repository: GuestbooksRepository) {
    suspend operator fun invoke(receiverId: String, senderId: String, content: String, image: String?): Result<Unit>{
        return try {
            val result = repository.insertGuestbook(
                receiverId = receiverId,
                senderId = senderId,
                content = content,
                image = image
            )
            Result.success(result)
        } catch (e: Exception){
            Log.e(Constants.TAG, "InsertGuestbookUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
package com.newBie.new_bie.core.block.domain.usecase

import android.util.Log
import com.newBie.new_bie.core.block.domain.repositories.BlockUserRepository
import com.newBie.new_bie.core.utils.Constants.TAG
import javax.inject.Inject

class InsertBlockUserUseCase @Inject constructor(private val repository: BlockUserRepository){
    suspend operator fun invoke(
        userId: String,
        blockedUserId: String
    ): Result<Unit>{
        return try {
            val result = repository.insertBlockUser(
                userId = userId,
                blockedUserId = blockedUserId
            )
            Result.success(result)
        } catch (e: Exception){
            Log.e(TAG, "InsertBlockUserUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
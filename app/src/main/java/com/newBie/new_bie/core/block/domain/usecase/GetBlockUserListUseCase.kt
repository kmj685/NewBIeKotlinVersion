package com.newBie.new_bie.core.block.domain.usecase

import android.util.Log
import com.newBie.new_bie.core.block.domain.entities.BlockUserEntity
import com.newBie.new_bie.core.block.domain.repositories.BlockUserRepository
import com.newBie.new_bie.core.utils.Constants.TAG
import javax.inject.Inject

class GetBlockUserListUseCase @Inject constructor(private val repository: BlockUserRepository) {
    suspend operator fun invoke(
        userId: String
    ): Result<List<BlockUserEntity>>{
        return try {
            val result = repository.getBlockUserList(
                userId = userId
            )
            Result.success(result)
        } catch (e: Exception){
            Log.e(TAG, "GetBlockUserListUseCase: ${e.message}", )
            Result.failure(e)
        }

    }
}
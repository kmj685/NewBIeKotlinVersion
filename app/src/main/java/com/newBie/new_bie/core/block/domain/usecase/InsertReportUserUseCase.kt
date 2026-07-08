package com.newBie.new_bie.core.block.domain.usecase

import android.util.Log
import com.newBie.new_bie.core.block.domain.repositories.BlockUserRepository
import com.newBie.new_bie.core.utils.Constants.TAG
import javax.inject.Inject

class InsertReportUserUseCase @Inject constructor(private val repository: BlockUserRepository){
    suspend operator fun invoke(senderId: String, reportedId: String, category: String, content: String?): Result<Unit>{
        return try {
            val result = repository.insertReportUser(
                senderId = senderId,
                reportedId = reportedId,
                category = category,
                content = content
            )

            Result.success(result)
        } catch (e: Exception){
            Log.e(TAG, "InsertReportUserUseCase: ${e.message}", )
            Result.failure(e)
        }
    }
}
package com.newBie.new_bie.features.profile.domain.usecase.followUseCase

import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetMyFollowingIdsUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend operator fun invoke(myId: String): Result<List<String>>{
        return try {
            val results = repository.getMyFollowingIds(myId)
            Result.success(results)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
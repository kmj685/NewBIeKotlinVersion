package com.newBie.new_bie.features.profile.domain.usecase.followUseCase

import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class FetchUserUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend operator fun invoke(targetUserId: String): Result<UserEntity?> {
        return try {
            val results = repository.fetchUser(targetUserId)
            Result.success(results)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
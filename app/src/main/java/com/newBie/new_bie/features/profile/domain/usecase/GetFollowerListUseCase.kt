package com.newBie.new_bie.features.profile.domain.usecase

import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository

class GetFollowerListUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(targetUserId: String): Result<List<FollowEntity>>{
        return try {
            val list = repository.getFollowers(targetUserId)
            Result.success(list)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
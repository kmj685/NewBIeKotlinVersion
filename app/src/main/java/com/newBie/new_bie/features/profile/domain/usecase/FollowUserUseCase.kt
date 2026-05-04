package com.newBie.new_bie.features.profile.domain.usecase

import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository

class FollowUserUseCase (private val repository: ProfileRepository){
    suspend operator fun invoke(myId: String, targetUserId: String): Result<Unit> {
        if (myId == targetUserId) {
            return Result.failure(Exception("자기 자신은 팔로우할 수 없습니다."))
        }
        return try{
            repository.followUser(myId, targetUserId)
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
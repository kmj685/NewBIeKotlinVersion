package com.newBie.new_bie.features.profile.domain.usecase.followUseCase

import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class UnfollowUserUseCase @Inject constructor(private val repository: ProfileRepository){
    suspend operator fun invoke(myId: String, targetUserId: String): Result<Unit> {
        if (myId == targetUserId) {
            return Result.failure(Exception("자기 자신은 언팔로우할 수 없습니다."))
        }
        return try{
            repository.unfollowUser(myId, targetUserId)
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
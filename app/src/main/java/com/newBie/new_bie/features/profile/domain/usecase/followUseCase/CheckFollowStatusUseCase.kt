package com.newBie.new_bie.features.profile.domain.usecase.followUseCase

import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class CheckFollowStatusUseCase @Inject constructor(private val repository: ProfileRepository){
    // 이런식으로 runCatching으로 편리하게 사용할 수도 있다.
    suspend operator fun invoke(myId: String, targetUserId: String): Result<Boolean> = runCatching {

        // 본인 확인
        if(myId == targetUserId) return runCatching { false }
           repository.isFollowing(myId = myId, targetUserId = targetUserId)
    }
}
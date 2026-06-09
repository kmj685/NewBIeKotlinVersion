package com.newBie.new_bie.features.profile.domain.usecase.followUseCase

import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend operator fun invoke(targetUserId: String): Result<UserEntity?> = runCatching{

        if(targetUserId.isBlank()) return@runCatching null

        repository.fetchUser(targetUserId)
    }
}
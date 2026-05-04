package com.newBie.new_bie.features.profile.domain.usecase

import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository

class GetUserUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(targetUserId: String): Result<UserEntity?> = runCatching{

        if(targetUserId.isBlank()) return@runCatching null

        repository.fetchUser(targetUserId)
    }
}
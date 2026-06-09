package com.newBie.new_bie.features.profile.domain.usecase.followUseCase

import android.util.Log
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class FetchUserPostsUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend operator fun invoke(
        targetUserId: String,
        currentIndex: Int,
        perPage: Int
        ): Result<List<PostWithProfileEntity>>{
        return try {
            val results = repository.fetchUserPosts(
                targetUserId = targetUserId,
                currentIndex = currentIndex,
                perPage = perPage
            )
            Result.success(results)
        } catch (e: Exception){
            Log.e(Constants.TAG, "마이페이지 유저 프로필: ${e.message}", )
            Result.failure(e)
        }
    }
}
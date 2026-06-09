package com.newBie.new_bie.features.profile.data.repositories

import com.newBie.new_bie.core.utils.getGridRange
import com.newBie.new_bie.features.post.domain.entities.NoticeEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.profile.data.datasources.ProfileDatasource
import com.newBie.new_bie.features.profile.data.dto.FollowDto
import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val datasource: ProfileDatasource) : ProfileRepository {

    override suspend fun fetchUser(targetUserId: String): UserEntity? {
        return datasource.fetchUser(targetUserId)
    }

    override suspend fun getFollowers(targetUserId: String): List<FollowEntity> {
        val dtoList = datasource.fetchFollowerList(targetUserId)
        return dtoList.map { it.toEntity() }
    }

    override suspend fun getFollowings(targetUserId: String): List<FollowEntity> {
        val dtoList = datasource.fetchFollowingList(targetUserId)
        return dtoList.map { it.toEntity() }
    }

    override suspend fun isFollowing(
        myId: String,
        targetUserId: String
    ): Boolean {
        return datasource.isFollowing(myId = myId, targetUserId = targetUserId)
    }

    override suspend fun followUser(myId: String, targetUserId: String) {
        datasource.followUser(myId = myId, targetUserId = targetUserId)
    }

    override suspend fun unfollowUser(myId: String, targetUserId: String) {
        datasource.unfollowUser(myId = myId, targetUserId = targetUserId)
    }

    override suspend fun getMyFollowingIds(myId: String): List<String> {
        // Datasource에서 팔로잉 리스트를 가져와 ID만 추출하여 반환
        return datasource.fetchFollowingList(myId).map { it.followingId }
    }

    override suspend fun fetchUserPosts(
        targetUserId: String,
        currentIndex: Int,
        perPage: Int
    ): List<PostWithProfileEntity> {
        // UtilFunctions에서 확장 함수 미리 만들어놓음
        val rangeString = getGridRange(currentIndex, perPage)

        return datasource.fetchUserPosts(
            range = rangeString,
            userId = targetUserId
        )
    }
}
package com.newBie.new_bie.features.profile.data.repositories

import com.newBie.new_bie.features.post.domain.entities.NoticeEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.profile.data.datasources.ProfileDatasource
import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository

class ProfileRepositoryImpl : ProfileRepository {

    val datasource = ProfileDatasource()

    override suspend fun fetchNotices(): List<NoticeEntity> {
        return emptyList()
    }

    override suspend fun getById(id: Int): NoticeEntity? {
        return null
    }

    override suspend fun fetchUser(targetUserId: String): UserEntity? {
        return datasource.fetchUser(targetUserId)
    }

    override suspend fun getFollowers(targetUserId: String): List<FollowEntity> {
        return datasource.fetchFollowerList(targetUserId)
    }

    override suspend fun getFollowings(targetUserId: String): List<FollowEntity> {
        return datasource.fetchFollowingList(targetUserId)
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
}
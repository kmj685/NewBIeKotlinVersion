package com.newBie.new_bie.features.profile.domain.repositories

import com.newBie.new_bie.features.post.domain.entities.NoticeEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import com.newBie.new_bie.features.profile.domain.entities.GuestbooksEntity

interface ProfileRepository {
    suspend fun fetchUser(targetUserId: String): UserEntity?
    // 조회
    suspend fun getFollowers(targetUserId: String): List<FollowEntity>
    suspend fun getFollowings(targetUserId: String): List<FollowEntity>

    // 상태 확인
    suspend fun isFollowing(myId: String, targetUserId: String): Boolean

    // 팔로우 언팔로우
    suspend fun followUser(myId: String, targetUserId: String)
    suspend fun unfollowUser(myId: String, targetUserId: String)
    suspend fun getMyFollowingIds(myId: String): List<String> // 내 팔로잉 ID 목록만 추출
    suspend fun fetchUserPosts(targetUserId: String, currentIndex: Int, perPage: Int): List<PostWithProfileEntity>
}
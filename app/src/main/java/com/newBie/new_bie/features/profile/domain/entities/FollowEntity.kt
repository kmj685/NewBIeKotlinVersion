package com.newBie.new_bie.features.profile.domain.entities

import com.newBie.new_bie.features.post.domain.entities.UserEntity

data class FollowEntity(
    val id: Int,
    val followerId: String,
    val followingId: String,
    val createdAt: String,
    val users: UserEntity,
    val isFollowing: Boolean // 서버 DB에는 없지만 UI에서 버튼 상태를 관리하기 위한 값
)
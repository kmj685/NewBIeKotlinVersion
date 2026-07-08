package com.newBie.new_bie.core.block.domain.repositories

import com.newBie.new_bie.core.block.domain.entities.BlockUserEntity

interface BlockUserRepository {
    // 유저-유저 차단
    suspend fun insertBlockUser(userId: String, blockedUserId: String)
    // 유저-유저 차단해제
    suspend fun deleteBlockUser(userId: String, blockedUserId: String)
    // 유저-유저 차단된 사용자 리스트
    suspend fun getBlockUserList(userId: String): List<BlockUserEntity>
    // 유저 신고
    suspend fun insertReportUser(senderId: String, reportedId: String, category: String, content: String?)
}
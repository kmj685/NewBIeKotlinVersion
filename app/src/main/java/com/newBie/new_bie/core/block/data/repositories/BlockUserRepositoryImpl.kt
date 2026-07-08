package com.newBie.new_bie.core.block.data.repositories

import com.newBie.new_bie.core.block.data.datasources.BlockUserDatasource
import com.newBie.new_bie.core.block.domain.entities.BlockUserEntity
import com.newBie.new_bie.core.block.domain.repositories.BlockUserRepository
import javax.inject.Inject

class BlockUserRepositoryImpl @Inject constructor(private val datasource: BlockUserDatasource): BlockUserRepository{
    override suspend fun insertBlockUser(userId: String, blockedUserId: String) {
        datasource.insertBlockUser(
            userId = userId,
            blockUserId = blockedUserId
        )
    }

    override suspend fun deleteBlockUser(userId: String, blockedUserId: String) {
        datasource.deleteBlockUser(
            userId = userId,
            blockUserId = blockedUserId
        )
    }

    override suspend fun getBlockUserList(userId: String): List<BlockUserEntity> {
        val dtoList = datasource.getBlockUserList(
            userId = userId
        )
        return dtoList.map { it.toEntity() }
    }

    override suspend fun insertReportUser(
        senderId: String,
        reportedId: String,
        category: String,
        content: String?
    ) {
        datasource.insertReportUser(
            senderId = senderId,
            reportedId = reportedId,
            category = category,
            content = content
        )
    }
}
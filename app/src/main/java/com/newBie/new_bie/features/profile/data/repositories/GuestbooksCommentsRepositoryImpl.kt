package com.newBie.new_bie.features.profile.data.repositories

import com.newBie.new_bie.features.profile.data.datasources.GuestbooksCommentsDatasource
import com.newBie.new_bie.features.profile.domain.entities.GuestbooksCommentsEntity
import com.newBie.new_bie.features.profile.domain.repositories.GuestbooksCommentsRepository
import javax.inject.Inject

class GuestbooksCommentsRepositoryImpl @Inject constructor(private val dataSource: GuestbooksCommentsDatasource): GuestbooksCommentsRepository {
    override suspend fun getGuestbooksCommentsList(guestbookId: Int): List<GuestbooksCommentsEntity> {
        val dtoList = dataSource.getGuestbooksCommentsList(
            guestbookId = guestbookId
        )
        return dtoList.map { it.toEntity() }
    }

    override suspend fun insertGuestbooksComment(
        guestbookId: Int,
        userId: String,
        content: String
    ) {
        dataSource.insertGuestbooksComment(
            guestbookId = guestbookId,
            userId = userId,
            content = content
        )
    }

    override suspend fun deletedGuestbooksComment(commentId: Int) {
        dataSource.deletedGuestbooksComment(
            commentId = commentId
        )
    }

    override suspend fun updatedGuestbooksComment(commentId: Int, content: String) {
        dataSource.updatedGuestbooksComment(
            commentId = commentId,
            content = content
        )
    }
}
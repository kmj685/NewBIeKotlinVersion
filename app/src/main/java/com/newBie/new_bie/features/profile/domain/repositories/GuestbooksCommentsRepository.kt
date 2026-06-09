package com.newBie.new_bie.features.profile.domain.repositories

import com.newBie.new_bie.features.profile.domain.entities.GuestbooksCommentsEntity

interface GuestbooksCommentsRepository {
    // 방명록 댓글 리스트 조회
    suspend fun getGuestbooksCommentsList(guestbookId: Int): List<GuestbooksCommentsEntity>
    // 방명록 댓글 입력
    suspend fun insertGuestbooksComment(guestbookId: Int, userId: String, content: String)
    // 방명록 댓글 삭제
    suspend fun deletedGuestbooksComment(commentId: Int)
    // 방명록 댓글 수정
    suspend fun updatedGuestbooksComment(commentId: Int, content: String)
}
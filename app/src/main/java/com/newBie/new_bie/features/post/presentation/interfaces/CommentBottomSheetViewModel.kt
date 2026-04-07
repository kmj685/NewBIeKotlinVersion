package com.newBie.new_bie.features.post.presentation.interfaces

import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow

interface CommentBottomSheetViewModel {
    val comments: MutableStateFlow<List<CommentWithProfileEntity>>
    val selectPostId: MutableStateFlow<Int?>
    val selectCommentId: MutableStateFlow<Int?>
    val userCommentInput: MutableStateFlow<String>
    val editUserCommentInput: MutableStateFlow<String>

    fun unSelectPostId()
    fun updateUserInput(input: String)
    fun updateEditUserInput(input: String)
    fun insertComment()

    fun deleteComment(id: Int, authorId: String)
    fun onSelectComment(commentId: Int, content: String)
    fun editComment(authorId: String)
    fun onCancel()
}
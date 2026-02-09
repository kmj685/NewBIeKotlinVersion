package com.newBie.new_bie.features.post.data.repositories

import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.LikesEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.SearchResultEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository

class PostRepositoryImpl : PostRepository {
    override suspend fun fetchPosts(
        orderBy: String,
        currentIndex: Int,
        category: String
    ): List<PostWithProfileEntity> {
        return emptyList()
    }

    override suspend fun fetchPostItem(id: Int): PostWithProfileEntity {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAuthorProfile(userId: String): UserEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getPostLikeCount(postId: Int): Int {
        return 0
    }

    override suspend fun fetchLikeItem(
        postId: Int,
        userId: String
    ): LikesEntity {
        TODO("Not yet implemented")
    }

    override suspend fun insertLike(postId: Int, userId: String) {
        return
    }

    override suspend fun cancelLike(postId: Int, userId: String) {
        return
    }

    override suspend fun fetchCommentIds(postId: Int): List<Int> {
        return emptyList()
    }

    override suspend fun fetchCommentItem(id: Int): CommentWithProfileEntity {
        TODO("Not yet implemented")
    }

    override suspend fun insertComment(
        postId: Int,
        userId: String,
        content: String
    ) {
        return
    }

    override suspend fun deleteComment(id: Int) {
        return
    }

    override suspend fun editComment(commentId: Int, content: String) {
        return
    }

    override suspend fun insertPost(
        userId: String,
        title: String,
        content: String,
        images: List<String>,
        categories: List<Int>
    ) {
        return
    }

    override suspend fun getCategoryList(): List<String> {
        return emptyList()
    }

    override suspend fun getCategoryTypeList(): List<CategoryTypeEntity> {
        return emptyList()
    }

    override suspend fun updatePost(
        postId: Int,
        title: String,
        content: String,
        images: List<String>,
        categories: List<Int>
    ) {
        return
    }

    override suspend fun searchAll(
        keyword: String,
        type: String,
        currentIndex: Int,
        perPage: Int
    ): SearchResultEntity {
        TODO("Not yet implemented")
    }

    override suspend fun deletePost(postId: Int) {
        return
    }

    override suspend fun fetchComments(postId: Int): List<CommentWithProfileEntity> {
        return emptyList()
    }
}
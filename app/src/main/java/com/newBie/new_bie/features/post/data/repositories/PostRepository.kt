package com.newBie.new_bie.features.post.data.repositories

import android.util.Log
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.core.utils.getRange
import com.newBie.new_bie.features.post.data.datasource.PostDatasource
import com.newBie.new_bie.features.post.data.dto.InsertPostRequestDTO
import com.newBie.new_bie.features.post.data.dto.UpdatePostDTO
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntityWithSupabase
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.LikesEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.SearchResultEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.post.domain.repositories.PostRepository

class PostRepositoryImpl : PostRepository {
    val datasource = PostDatasource()

    override suspend fun fetchPosts(
        orderBy: String,
        currentIndex: Int,
        category: String,
        perPage: Int
    ): List<PostWithProfileEntity> {

        val range = getRange(currentIndex, perPage)

        return datasource.fetchPosts(orderBy = orderBy, category = category, range = range)
    }

    override suspend fun fetchPostItem(id: Int): PostWithProfileEntity? {
        return datasource.fetchPostItem(id)
    }

    override suspend fun fetchAuthorProfile(userId: String): UserEntity? {
        return datasource.fetchAuthorProfile(userId)
    }

    override suspend fun getPostLikeCount(postId: Int): Int {
        return 0
    }

    override suspend fun fetchLikeItem(
        postId: Int,
        userId: String
    ): LikesEntity? {
        return fetchLikeItem(postId, userId)
    }

    override suspend fun insertLike(postId: Int, userId: String) {
        datasource.insertLike(postId, userId)
    }

    override suspend fun cancelLike(postId: Int, userId: String) {
        datasource.cancelLike(postId, userId)
    }

    override suspend fun fetchCommentIds(postId: Int): List<Int> {
        return emptyList()
    }

    override suspend fun fetchCommentItem(id: Int): CommentWithProfileEntity? {
        return datasource.fetchCommentItem(id)
    }

    override suspend fun insertComment(
        postId: Int,
        userId: String,
        content: String
    ) {
        datasource.insertComment(postId,userId, content)
    }

    override suspend fun deleteComment(id: Int) {
        datasource.deleteComment(id)
    }

    override suspend fun editComment(commentId: Int, content: String) {
        datasource.editComment(commentId, content)
    }

    override suspend fun insertPost(
        userId: String,
        title: String,
        content: String,
        images: List<String>,
        categories: List<Int>
    ) {
        datasource.insertPost(InsertPostRequestDTO(author_id = userId,title = title, content =content,images = images,categories =categories))
    }

    override suspend fun getCategoryList(): List<String> {
        Log.d(Constants.TAG, "리포지토리 getCategoryList() ")
        return datasource.getCategoryList()
    }

    override suspend fun getCategoryTypeList(): List<CategoryTypeEntityWithSupabase> {
        return datasource.getCategoryTypeList()
    }

    override suspend fun updatePost(
        postId: Int,
        title: String,
        content: String,
        images: List<String>,
        categories: List<Int>
    ) {
        datasource.updatePost(postId, UpdatePostDTO(title = title, content = content, images = images, categories = categories))
    }

    override suspend fun searchAll(
        keyword: String,
        type: String,
        currentIndex: Int,
        perPage: Int
    ): SearchResultEntity {
        val range : String = getRange(currentIndex,perPage)
        return datasource.searchAll(range,keyword,type)
    }

    override suspend fun deletePost(postId: Int) {
        datasource.deletePost(postId)
    }

    override suspend fun fetchComments(postId: Int): List<CommentWithProfileEntity> {
        return datasource.fetchComments(postId)
    }
}
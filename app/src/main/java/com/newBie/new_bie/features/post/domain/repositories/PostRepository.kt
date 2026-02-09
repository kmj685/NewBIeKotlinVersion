package com.newBie.new_bie.features.post.domain.repositories

import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.LikesEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.SearchResultEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity

interface PostRepository {
    suspend fun fetchPosts(orderBy : String, currentIndex : Int = 1, category : String) : List<PostWithProfileEntity>

    suspend fun fetchPostItem(id : Int) : PostWithProfileEntity

    suspend fun fetchAuthorProfile(userId : String) : UserEntity

    suspend fun getPostLikeCount(postId : Int) : Int

    suspend fun fetchLikeItem(postId: Int, userId : String) : LikesEntity

    suspend fun insertLike(postId : Int, userId : String)
    suspend fun cancelLike(postId : Int, userId : String)
    suspend fun fetchCommentIds(postId : Int) : List<Int>
    suspend fun fetchCommentItem(id : Int) : CommentWithProfileEntity
    suspend fun insertComment(postId : Int, userId : String, content : String)
    suspend fun deleteComment(id : Int)
    suspend fun editComment(commentId : Int, content : String)
    suspend fun insertPost(userId : String, title : String, content : String, images : List<String>, categories : List<Int>)
    suspend fun getCategoryList() : List<String>
    suspend fun getCategoryTypeList() : List<CategoryTypeEntity>
    suspend fun updatePost(postId : Int, title : String, content : String, images : List<String>, categories : List<Int>)
    suspend fun searchAll(keyword : String, type : String = "all", currentIndex : Int = 1, perPage : Int = 5) : SearchResultEntity
    suspend fun deletePost(postId : Int)
    suspend fun fetchComments(postId : Int) : List<CommentWithProfileEntity>


}
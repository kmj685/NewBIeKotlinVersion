package com.newBie.new_bie.features.post.data.datasource

import com.newBie.new_bie.core.utils.API
import com.newBie.new_bie.features.post.data.dto.ActionResponse
import com.newBie.new_bie.features.post.data.dto.BaseResponse
import com.newBie.new_bie.features.post.data.dto.CommentWithProfileDto
import com.newBie.new_bie.features.post.data.dto.InsertPostRequestDto
import com.newBie.new_bie.features.post.data.dto.LikesCountDto
import com.newBie.new_bie.features.post.data.dto.PostDto
import com.newBie.new_bie.features.post.data.dto.PostWithProfileDto
import com.newBie.new_bie.features.post.data.dto.SearchResultDto
import com.newBie.new_bie.features.post.data.dto.UpdatePostDto
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.LikesCountEntity
import com.newBie.new_bie.features.post.domain.entities.PostEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.SearchResultEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PostRetrofitInterface {
    @GET("posts/{id}")
    suspend fun fetchPostItem(
        @Path("id") id : Int,
//        @Header("Authorization") authorizationKey : String = API.AUTHORIZATION,
        @Header("Content-Type") contentType : String = "application/json",


        ) : BaseResponse<PostWithProfileDto>

    @GET("posts")
    suspend fun fetchPosts(
//        @Header("Authorization") authorizationKey : String = API.AUTHORIZATION,
        @Header("Content-Type") contentType : String = "application/x-www-form-urlencoded",
//        @Header("Content-Type") contentType : String = API.CONTENT_TYPE,
        @Header("Range") range : String,
        @Query("orderBy") orderBy : String,
        @Query("category") category : String
    ) : BaseResponse<List<PostWithProfileDto>>

    @GET("posts/{id}/likes_count")
    suspend fun getPostLikeCount(
        @Path("id") id: Int,
//        @Header("Authorization") authorizationKey : String = API.AUTHORIZATION,
        @Header("Content-Type") contentType : String = "application/json",
    ) : BaseResponse<LikesCountDto>

    @GET("comments/{id}")
    suspend fun fetchCommentItem(
        @Path("id") id: Int,
//        @Header("Authorization") authorization: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = API.CONTENT_TYPE
    ): BaseResponse<CommentWithProfileDto>

    @GET("comment")
    suspend fun fetchComments(
        @Query("post_id") postId: Int,
//        @Header("Authorization") authorization: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = API.CONTENT_TYPE
    ): BaseResponse<List<CommentWithProfileDto>>

    @POST("posts")
    suspend fun insertPost(
        @Body body: InsertPostRequestDto,
//        @Header("Authorization") authorizationKey: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = "application/json"
    ): BaseResponse<PostDto>

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int,
//        @Header("Authorization") authorizationKey: String = API.AUTHORIZATION
    ): ActionResponse

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body body: UpdatePostDto,
//        @Header("Authorization") authorizationKey: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = "application/json"
    ): ActionResponse

    @GET("search")
    suspend fun searchAll(
//        @Header("Authorization") authorizationKey: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Header("Range") range: String,
        @Query("keyword") keyword: String,
        @Query("type") type: String
    ): SearchResultDto


    @GET("users_posts")
    suspend fun fetchUserPosts(
//        @Header("Authorization") authorizationKey: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Header("Range") range: String,
        @Query("userId") userId: String
    ): BaseResponse<List<PostWithProfileDto>>
}
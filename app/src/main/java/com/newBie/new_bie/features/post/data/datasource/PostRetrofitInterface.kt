package com.newBie.new_bie.features.post.data.datasource

import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.API
import com.newBie.new_bie.features.post.data.dto.ActionResponse
import com.newBie.new_bie.features.post.data.dto.BaseResponse
import com.newBie.new_bie.features.post.data.dto.InsertPostRequestDTO
import com.newBie.new_bie.features.post.data.dto.PostWithProfileResponseDTO
import com.newBie.new_bie.features.post.data.dto.UpdatePostDTO
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.LikesCountEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.SearchResultEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PostRetrofitInterface {
    @GET("posts/{id}")
    suspend fun fetchPostItem(
        @Path("id") id : Int,
//        @Header("Authorization") authorizationKey : String = API.AUTHORIZATION,
        @Header("Content-Type") contentType : String = "application/json",


        ) : BaseResponse<PostWithProfileEntity>

    @GET("posts")
    suspend fun fetchPosts(
//        @Header("Authorization") authorizationKey : String = API.AUTHORIZATION,
        @Header("Content-Type") contentType : String = "application/x-www-form-urlencoded",
//        @Header("Content-Type") contentType : String = API.CONTENT_TYPE,
        @Header("Range") range : String,
        @Query("orderBy") orderBy : String,
        @Query("category") category : String
    ) : BaseResponse<List<PostWithProfileEntity>>

    @GET("posts/{id}/likes_count")
    suspend fun getPostLikeCount(
        @Path("id") id: Int,
//        @Header("Authorization") authorizationKey : String = API.AUTHORIZATION,
        @Header("Content-Type") contentType : String = "application/json",
    ) : BaseResponse<LikesCountEntity>

    @GET("comments/{id}")
    suspend fun fetchCommentItem(
        @Path("id") id: Int,
//        @Header("Authorization") authorization: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = API.CONTENT_TYPE
    ): BaseResponse<CommentWithProfileEntity>

    @GET("comment")
    suspend fun fetchComments(
        @Query("post_id") postId: Int,
//        @Header("Authorization") authorization: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = API.CONTENT_TYPE
    ): BaseResponse<List<CommentWithProfileEntity>>

    @POST("posts")
    suspend fun insertPost(
        @Body body: InsertPostRequestDTO,
//        @Header("Authorization") authorizationKey: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = "application/json"
    ): BaseResponse<PostWithProfileEntity>

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int,
//        @Header("Authorization") authorizationKey: String = API.AUTHORIZATION
    ): ActionResponse

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body body: UpdatePostDTO,
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
    ): SearchResultEntity


    @GET("users_posts")
    suspend fun fetchUserPosts(
//        @Header("Authorization") authorizationKey: String = API.AUTHORIZATION,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Header("Range") range: String,
        @Query("userId") userId: String
    ): BaseResponse<List<PostWithProfileEntity>>
}
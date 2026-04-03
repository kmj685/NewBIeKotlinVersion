package com.newBie.new_bie.features.post.data.datasource

import android.util.Log
import com.newBie.new_bie.core.managers.RetrofitManager
import com.newBie.new_bie.core.managers.SupabaseManager
//import com.newBie.new_bie.core.managers.SupabaseManager.supabase
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.data.dto.ActionResponse
import com.newBie.new_bie.features.post.data.dto.CategoryTypeDto
import com.newBie.new_bie.features.post.data.dto.CategoryTypeDtoWithSupabase
import com.newBie.new_bie.features.post.data.dto.CategoryTypeTitleDto
import com.newBie.new_bie.features.post.data.dto.InsertPostRequestDto
import com.newBie.new_bie.features.post.data.dto.LikesDto
import com.newBie.new_bie.features.post.data.dto.PostDto
import com.newBie.new_bie.features.post.data.dto.UpdatePostDto
import com.newBie.new_bie.features.post.data.dto.UserDto
import com.newBie.new_bie.features.post.data.dto.UserDtoWithSupabase
import com.newBie.new_bie.features.post.data.mapper.toEntity
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.LikesCountEntity
import com.newBie.new_bie.features.post.domain.entities.LikesEntity
import com.newBie.new_bie.features.post.domain.entities.PostEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.SearchResultEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class PostDatasource {
    val _supabase = SupabaseManager.supabase
    private var _retrofit = RetrofitManager.retrofit


    fun getAuthorizationKey() : String {
        val session = SupabaseManager.supabase.auth.currentSessionOrNull()
        val accessToken = session?.accessToken
        if (accessToken != null) return "Bearer: ${accessToken}"
        else return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InN5ZmdmaWNjZWpqZ3R2cG10a3p4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjIwNTUwNjksImV4cCI6MjA3NzYzMTA2OX0.Ng9atODZnfRocZPtnIb74s6PLeIJ2HqqSaatj1HbRsc"
    }

    private val api: PostRetrofitInterface =
        _retrofit.create(PostRetrofitInterface::class.java)

    // 게시글 단건 조회
    suspend fun fetchPostItem(id: Int): PostWithProfileEntity? {

        val dto = api.fetchPostItem(id).data
        Log.d(Constants.TAG, "fetchPostItem의 dto : ${dto} ")
        return dto?.toEntity()
    }

    // 게시글 리스트 조회
    suspend fun fetchPosts(
        range: String,
        orderBy: String,
        category: String
    ): List<PostWithProfileEntity> {
        Log.d(Constants.TAG, "fetchPosts: accessToken : ${getAuthorizationKey()}")
        val response = api.fetchPosts(range = range, orderBy = orderBy, category = category)
        Log.d(Constants.TAG, "fetchPosts: ${response}")


        if (response == null) {
            return emptyList()
        }
        else {
            val list = response.data?.map { it-> it.toEntity() }
            return list as List<PostWithProfileEntity>
        }
    }

    // 좋아요 개수
    suspend fun getPostLikeCount(id: Int): LikesCountEntity? {
        return api.getPostLikeCount(id = id).data?.toEntity()
    }

    // 댓글 단건
    suspend fun fetchCommentItem(id: Int): CommentWithProfileEntity? {
        return api.fetchCommentItem(id = id).data?.toEntity()
    }

    // 댓글 리스트
    suspend fun fetchComments(postId: Int): List<CommentWithProfileEntity> {
        val response = api.fetchComments(postId=postId).data?.map { it-> it.toEntity() }
        if (response == null) return emptyList()
        else return response
    }

    // 게시글 생성
    suspend fun insertPost(body: InsertPostRequestDto): PostEntity? {
        return api.insertPost(body = body).data?.toEntity()
    }

    // 게시글 삭제
    suspend fun deletePost(id: Int): ActionResponse {
        return api.deletePost(id=id)
    }

    // 게시글 수정
    suspend fun updatePost(
        id: Int,
        body: UpdatePostDto
    ): ActionResponse {
        return api.updatePost(id=id, body=body)
    }

    // 검색
    suspend fun searchAll(
        range: String,
        keyword: String,
        type: String
    ): SearchResultEntity {
        val result = api.searchAll(
            range = range,
            keyword = keyword,
            type = type
        )
        Log.d(Constants.TAG, "searchAll result: ${result}")
        return result.toEntity()
    }

    // 특정 유저 게시글
    suspend fun fetchUserPosts(
        range: String,
        userId: String
    ): List<PostWithProfileEntity> {
        val response = api.fetchUserPosts(range = range, userId = userId).data?.map { it-> it.toEntity() }
        if (response == null) return emptyList()
        else return response
    }

    suspend fun getCategoryList() :List<String> {
        val response = _supabase.from("category_type").select(columns = Columns.list("type_title")).decodeList<CategoryTypeTitleDto>()
        Log.d(Constants.TAG, "getCategoryList: ${response}")
        return response.map { it.typeTitle }
        }

    // ✅ 카테고리 리스트
    suspend fun getCategoryTypeList(): List<CategoryTypeEntity> {

        val result = _supabase
            .from("category_type")
            .select()
            .decodeList<CategoryTypeDtoWithSupabase>()

        val list = result.map { it -> it.toEntity() }

        return list.ifEmpty { emptyList() }
    }

    // ✅ 작성자 프로필 조회
    suspend fun fetchAuthorProfile(userId: String): UserEntity {

        val result = _supabase
            .from("users")
            .select()
            {
                filter{
                    eq("id", userId)
                }
            }
            .decodeList<UserDtoWithSupabase>()
        val list = result.map { it -> it.toEntity() }

        return list.first()
    }

    // ✅ 좋아요 단건 조회
    suspend fun fetchLikeItem(
        postId: Int,
        userId: String
    ): LikesEntity? {

        val result = _supabase
            .from("likes")
            .select()
            {
                filter{
                    eq("user_id", userId)
                    eq("post_id", postId)
                }
            }
            .decodeList<LikesDto>()

        val list = result.map { it -> it.toEntity() }

        return list.firstOrNull()
    }

    // ✅ 좋아요 추가
    suspend fun insertLike(
        postId: Int,
        userId: String
    ) {
        _supabase
            .from("likes")
            .insert(
                buildJsonObject {
                    put("post_id", postId)
                    put("user_id", userId)
                }
            )
    }

    // ✅ 좋아요 취소
    suspend fun cancelLike(
        postId: Int,
        userId: String
    ) {
        _supabase
            .from("likes")
            .delete {
                filter{
                    eq("post_id", postId)
                    eq("user_id", userId)
                }
            }
    }

    // ✅ 댓글 ID 목록
    suspend fun fetchCommentIds(postId: Int): List<Int> {

        val result = _supabase
            .from("comments")
            .select(columns = Columns.list("id")) {
                filter{
                    eq("post_id", postId)
                }
            }
            .decodeList<Map<String, Int>>()

        return result.map { it["id"] ?: 0 }
    }

    // ✅ 댓글 작성
    suspend fun insertComment(
        postId: Int,
        userId: String,
        content: String
    ) {
        _supabase
            .from("comments")
            .insert(
                buildJsonObject {
                    put("post_id", postId)
                    put("author_id", userId)
                    put("content", content)
                }
            )
    }

    // ✅ 댓글 삭제
    suspend fun deleteComment(id: Int) {
        _supabase
            .from("comments")
            .delete {
                filter{
                    eq("id", id)
                }
            }
    }

    // ✅ 댓글 수정
    suspend fun editComment(
        commentId: Int,
        content: String
    ) {
        _supabase
            .from("comments")
            .update(
                buildJsonObject {
                    put("content", content)
                }
            ) {
                filter{
                    eq("id", commentId)
                }
            }
    }
}
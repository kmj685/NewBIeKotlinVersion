package com.newBie.new_bie.features.profile.data.datasources

import android.util.Log
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.profile.data.dto.GuestbooksCommentsDto
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class GuestbooksCommentsDatasource @Inject constructor() {
    private val _supabase = SupabaseManager.supabase

    suspend fun getGuestbooksCommentsList(guestbookId: Int): List<GuestbooksCommentsDto>{

        return try {
            _supabase.from("guestbooks_comments")
                .select(
                    columns = Columns.raw(
                        """
                        id,
                        guestbooks_id,
                        content,
                        created_at,
                        deleted_at,
                        is_block,
                        author_id:users!guestbooks_comments_author_id_fkey(*)
                    """.trimIndent()
                    )
                ) {
                    order(
                        column = "created_at", order = Order.DESCENDING
                    )
                    filter {
                        eq("guestbooks_id", guestbookId)
                    }
                }
                .decodeList<GuestbooksCommentsDto>()
        } catch (e: Exception){
            Log.e(TAG, "getGuestbooksCommentsList: ${e.message}", )
            emptyList()
        }
    }

    suspend fun insertGuestbooksComment(
        guestbookId: Int,
        userId: String,
        content: String
    ){
        _supabase
            .from("guestbooks_comments")
            // 이터베이스의 author_id 컬럼은 유저의 고유 아이디 문자열(text 또는 uuid) 하나만 받도록 되어 있습니다.
            // 그런데 이 DTO를 그대로 insert에 던지면, Supabase는 authorId 컬럼 자리에 {"id": "...", "nickName": "..."} 같은 유저 객체 JSON 전체를 쑤셔 넣으려고 시도합니다. 여기서 데이터 타입이 맞지 않아 디비가 에러를 뱉는 것입니다.
            // 그래서 buildJsonObject를 사용해서 author_id를 String으로 자동적으로 넣어줌
            // buildJsonObject란? 잡한 클래스(DTO)를 미리 정의하지 않고도, 코틀린 코드 블록({}) 안에서 JSON 구조를 가독성 좋고 직관적으로 뚝딱 만들어낼 수 있게 도와주는 아주 유용한 도구입니다.
            .insert(
                buildJsonObject {
                    put("guestbooks_id", guestbookId)
                    put("author_id", userId)
                    put("content", content)
                }
            )
    }

    suspend fun deletedGuestbooksComment(
        commentId: Int
    ){
        _supabase
            .from("guestbooks_comments")
            .delete {
                filter {
                    eq("id", commentId)
                }
            }
    }

    suspend fun updatedGuestbooksComment(
        commentId: Int,
        content: String
    ){
        _supabase
            .from("guestbooks_comments")
            .update (
                buildJsonObject {
                    put("content", content)
                }
            ) {
                filter {
                    eq("id", commentId)
                }
            }
    }
}
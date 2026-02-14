package com.newBie.new_bie.sampleCode

import com.newBie.new_bie.App
import com.newBie.new_bie.core.utils.SupabaseInitial
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter

object SampleSupabaseManager {

//    val context = App.instance
//    val supabase = createSupabaseClient(
//        supabaseUrl = SupabaseInitial.URL,
//        supabaseKey = SupabaseInitial.ANON_KEY
//    ) {
//        install(Auth)
//        install(Postgrest)
//    }
//    val userId = MutableStateFlow<String>("")
//    var userInfoState = MutableStateFlow<Users?>(null)
//    val recommendQuestList = MutableStateFlow<List<RecommendQuest>>(listOf())
//    val noticeList = MutableStateFlow<List<Notice>>(listOf())
//    init {
//        fetchRecommendQuest()
//        fetchNotice()
//    }
//    val questRecordList = MutableStateFlow<List<QuestRecord>>(listOf())
//    val questSet = MutableStateFlow<QuestSet?>(null)
//    val isUnregister = MutableStateFlow<String?>(userInfoState.value?.unregisterAt)
//
//    fun register(email : String, password : String, success : () -> Unit = {}, fail : (String) -> Unit = {}) {
//        GlobalScope.launch {
//            try {
//                supabase.auth.signUpWith(Email) {
//                    this.email = email
//                    this.password = password
//                }
//                success.invoke()
//                print("회원가입 성공")
//            } catch (e: Exception) {
//                print("회원가입 실패: ${e.message}")
//                val message = if(e.message == alreadyRegistered) "이미 가입된 이메일 입니다" else if(e.message == isNotEmail) "이메일 주소를 확인하십시오" else "${e.message}"
//                fail(message)
////                SupabaseManager.insertUserRecommend("60f156f4-e4cd-46c6-a64e-ae03e5a6209e", "${e.message}")
//
//            }
//
//        }
//    }
//    fun login(email : String, password : String, success : () -> Unit = {}, fail : () -> Unit = {} ) {
//        GlobalScope.launch {
//            try {
//                supabase.auth.signInWith(Email) {
//                    this.email = email
//                    this.password = password
//                }
//                print("로그인 성공")
//                success.invoke()
////                Toast.makeText(context, "로그인 성공",
////                    Toast.LENGTH_SHORT).show()
//            } catch (e: Exception) {
//                print("로그인 실패: ${e.message}")
//                fail.invoke()
////                Toast.makeText(context, "아이디와 비밀번호를 다시 확인하십시오.",
////                    Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    suspend fun fetchUser(id: String){
//        val user = supabase.from("users").select() {
//            filter {
//                Users::id eq id
//            }
//        }.decodeSingle<Users>()
//        SupabaseManager.userInfoState.emit(user)
//    }
//
//    fun fetchRecommendQuest(){
//        GlobalScope.launch {
//            val recommendQuestList = supabase.from("recommend_quest").select()
//                .decodeList<RecommendQuest>()
//            SupabaseManager.recommendQuestList.emit(recommendQuestList)
//        }
//
//    }
//
//    fun logout(toastMesage : () -> Unit = {}) {
//        GlobalScope.launch {
//            supabase.auth.signOut()
//
//        }
//    }
//    fun insertQuestRecord(id: String) {
//        GlobalScope.launch {
//            val questSet = SupabaseManager.downloadQuestSet(id)
//            supabase.from("quest_record").insert(questSet)
//        }
//    }
//    fun insertQuestRecord(item : InsertQuestSet) {
//        GlobalScope.launch {
//            supabase.from("quest_record").insert(item)
//        }
//    }
//
//    suspend fun fetchQuestRecord(id: String) {
//        val questRecords = supabase.from("quest_record").select() {
//            filter {
//                QuestRecord::userId eq id
//            }
//        }.decodeList<QuestRecord>()
//        SupabaseManager.questRecordList.emit(questRecords.sortedWith(compareByDescending{it.id}))
//    }
//
//    suspend fun fetchQuestSet(id: String) :QuestSet? {
//        try {
//            val setting : QuestSet = supabase.from("quest_set").select() {
//                filter {
//                    QuestSet::userId eq id
//                }
//            }.decodeSingle<QuestSet>()
//            questSet.emit(setting)
//            return setting
//        } catch (e : Exception) {
//            return null
//        }
//    }
//
//    fun insertQuestSet(questSet : InsertQuestSet) {
//        GlobalScope.launch {
//            val questSet = questSet
//            supabase.from("quest_set").insert(questSet)
//        }
//    }
//    fun insertQuestRecordGlobal(id : String) {
//        GlobalScope.launch {
//            val questRecords = supabase.from("quest_record").select() {
//                filter {
//                    QuestRecord::userId eq id
//                }
//            }.decodeList<QuestRecord>()
//            questRecordList.emit(questRecords.sortedWith(compareByDescending{it.id}))
//        }
//    }
//    suspend fun downloadQuestSet(id: String) :InsertQuestSet {
//        val setting : InsertQuestSet = supabase.from("quest_set").select() {
//            filter {
//                InsertQuestSet::userId eq id
//            }
//        }.decodeSingle<InsertQuestSet>()
//        return setting
//    }
//    fun updateQuestRecord(id : Int, isDone1 : Boolean, isDone2 : Boolean, isDone3 : Boolean) {
//        GlobalScope.launch {
//            supabase.from("quest_record").update(
//                {
//                    QuestRecord::isDone1 setTo isDone1
//                    QuestRecord::isDone2 setTo isDone2
//                    QuestRecord::isDone3 setTo isDone3
//                }
//            ) {
//                filter {
//                    QuestRecord::id eq id
//                }
//            }
//            userInfoState.value?.let {
//                SupabaseManager.fetchQuestRecord(it.id)
//            }
//
//        }
//    }
//    // 사용자 추천 퀘스트
//    fun insertUserRecommend(id: String, title: String) {
//        GlobalScope.launch {
//            val quest = UserRecommend(userId = id, title = title)
//            supabase.from("user_recommend_quest").insert(quest)
////            try {
////                val quest = insertUserRecommend(id, title)
////                supabase.from("user_recommend_quest").insert(quest)
////                print("성공")
////            } catch (e : Exception) {
////                print("실패, ")
////            }
//
//        }
//    }
//    fun insertUserEnquiry(id: String, title: String) {
//        GlobalScope.launch {
//            val enquiry = InsertUserEnquiry(userId = id, title = title)
//            supabase.from("enquiry").insert(enquiry)
////            try {
////                val quest = insertUserRecommend(id, title)
////                supabase.from("user_recommend_quest").insert(quest)
////                print("성공")
////            } catch (e : Exception) {
////                print("실패, ")
////            }
//        }
//    }
//    fun updateUserNickname(id: String, inputNickname: String) {
//        GlobalScope.launch {
//            supabase.from("users").update(
//                {
//                    set("nickName", inputNickname)
//                }
//            ) {
//                filter {
//                    Users::id eq id
//                }
//            }
//            delay(1000)
//            fetchUser(id)
//        }
//    }
//
//    fun fetchNotice() {
//        GlobalScope.launch {
//            val notices = supabase.from("notice").select()
//                .decodeList<Notice>()
//            SupabaseManager.noticeList.emit(notices)
//        }
//    }
//
//    fun fetchUserQuestSet(id : String) {
//        GlobalScope.launch {
//            val setting : QuestSet = supabase.from("quest_set").select() {
//                filter {
//                    QuestSet::userId eq id
//                }
//            }.decodeSingle<QuestSet>()
//            questSet.emit(setting)
//        }
//
//    }
//
//    fun updateQuestSet(id: Int, userId : String, questSet: InsertQuestSet) {
//        GlobalScope.launch {
//            supabase.from("quest_set").update(
//                {
//                    set("quest1", questSet.quest1)
//                    set("quest2", questSet.quest2)
//                    set("quest3", questSet.quest3)
//                }
//            ) {
//                filter {
//                    QuestSet::id eq id
//                }
//            }
//            delay(1000)
//            fetchUserQuestSet(userId)
//        }
//    }
//
//    fun updateTodayQuest(id: Int, userId : String, questSet: InsertQuestSet) {
//        GlobalScope.launch {
//            supabase.from("quest_record").update(
//                {
//                    set("quest1", questSet.quest1)
//                    set("quest2", questSet.quest2)
//                    set("quest3", questSet.quest3)
//                    set("is_done1", false)
//                    set("is_done2", false)
//                    set("is_done3", false)
//                }
//            ) {
//                filter {
//                    QuestRecord::id eq id
//                }
//            }
//            delay(1000)
//            fetchQuestRecord(userId)
//        }
//    }
//    fun storeUserId(id: String) {
//        GlobalScope.launch {
//            SupabaseManager.userId.emit(id)
//        }
//    }
//    fun deleteUser(id :String) {
//        GlobalScope.launch {
//            val currentTimestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
//            supabase.from("users").update(
//                {
//                    set("unregister_at", currentTimestamp)
//                }
//            ) {
//                filter {
//                    Users::id eq id
//                }
//            }
//            delay(1000)
//            logout()
////            supabase.auth.admin.deleteUser(uid = id)
//        }
//    }
//    fun unRegister(id :String) {
//        GlobalScope.launch {
////            logout()
//            supabase.auth.admin.deleteUser(uid = id)
//            val currentTimestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
//        }
//    }
}
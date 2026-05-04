package com.newBie.new_bie.features.profile.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.profile.data.repositories.ProfileRepositoryImpl
import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import com.newBie.new_bie.features.profile.domain.usecase.CheckFollowStatusUseCase
import com.newBie.new_bie.features.profile.domain.usecase.FollowUserUseCase
import com.newBie.new_bie.features.profile.domain.usecase.GetFollowerListUseCase
import com.newBie.new_bie.features.profile.domain.usecase.GetFollowingListUseCase
import com.newBie.new_bie.features.profile.domain.usecase.GetUserUseCase
import com.newBie.new_bie.features.profile.domain.usecase.UnfollowUserUseCase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FollowViewModel : ViewModel() {
    private val _repository: ProfileRepository = ProfileRepositoryImpl()

    // UseCase 정의
    private val getFollowerListUseCase = GetFollowerListUseCase(_repository)
    private val getFollowingListUseCase = GetFollowingListUseCase(_repository)
    private val getFetchUserUseCase = GetUserUseCase(_repository)
    private val followUserUseCase = FollowUserUseCase(_repository)
    private val unfollowUserUseCase = UnfollowUserUseCase(_repository)
    private val checkFollowStatusUseCase = CheckFollowStatusUseCase(_repository)

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user = _user.asStateFlow()

    private val _targetUserId = MutableStateFlow<String?>(null)

    private val _followerList = MutableStateFlow<List<FollowEntity>>(emptyList())
    val followerList = _followerList.asStateFlow()
    private val _followingList = MutableStateFlow<List<FollowEntity>>(emptyList())
    val followingList = _followingList.asStateFlow()

    val myId = SupabaseManager.supabase.auth.currentUserOrNull()?.id

    // 팔로잉 여부
    private val _isFollowing = MutableStateFlow(false)
    val isFollowing: StateFlow<Boolean> = _isFollowing

    // 1. 팔로워 리스트 가져오기 (성능 최적화 버전)
    suspend fun fetchFollowerList() {
        val targetId = _targetUserId.value ?: return
        val myId = SupabaseManager.supabase.auth.currentUserOrNull()?.id

        val result = getFollowerListUseCase(targetUserId = targetId)

        result.onSuccess { list ->
            // 핵심: 내 팔로잉 ID 리스트를 한 번만 가져옴 (N+1 문제 해결)
            val myFollowingIds = if (myId != null) {
                _repository.getMyFollowingIds(myId)
            } else emptyList()

            val mappedList = list.map { item ->
                val userIdInList = item.users.id
                // 내 팔로잉 목록에 이 유저가 포함되어 있는지 확인
                val isActuallyFollowing = myFollowingIds.contains(userIdInList)

                item.copy(isFollowing = isActuallyFollowing)
            }
            _followerList.value = mappedList
        }.onFailure {
            _followerList.value = emptyList()
        }
    }

    suspend fun fetchFollowingList(){
        val userId = _targetUserId.value ?: return
        val myId = SupabaseManager.supabase.auth.currentUserOrNull()?.id

        // UseCase 호출
        val result = getFollowingListUseCase(targetUserId = userId)

        result.onSuccess { list ->
            // map을 사용하여 리스트의 각 아이템을 변환합니다.
            val mappedFollowerList = list.map { item ->
                val targetUserIdInList = item.users.id ?: ""

                // 1. 본인이거나 ID가 없으면 체크할 필요 없이 false
                if (myId == null || targetUserIdInList.isEmpty() || myId == targetUserIdInList) {
                    item.copy(isFollowing = false)
                } else {
                    // 2. UseCase를 직접 호출해서 Boolean 결과를 즉시 받습니다. (비동기 launch 금지)
                    val isFollowingStatus = checkFollowStatusUseCase(
                        myId = myId,
                        targetUserId = targetUserIdInList
                    ).getOrDefault(false)

                    // 3. 팔로우 상태를 입힌 복사본 반환
                    item.copy(isFollowing = isFollowingStatus)
                }
            }
            _followingList.value = mappedFollowerList
        }.onFailure {
            _followingList.value = emptyList()
        }
    }

    suspend fun fetchUser(){
        val userId = _targetUserId.value ?: return

        val result = getFetchUserUseCase(targetUserId = userId)

        result.onSuccess {
            _user.value = it

            checkFollowStatUs(userId) // 팔로우 상태 확인
        }.onFailure {
            _user.value = null
        }
    }

    fun loadData(targetUserId: String?) {
        viewModelScope.launch {
            val userId = targetUserId ?: SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: return@launch

            _targetUserId.value = userId

            fetchUser()
            fetchFollowerList()
            fetchFollowingList()
        }
    }

    fun toggleFollow(targetUserId: String) {
        viewModelScope.launch {
            // 1. 내 ID 확인: 현재 로그인한 사용자의 ID를 가져옵니다. 없으면 중단합니다.
            val myId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: return@launch

            // 2. 데이터 복사: 현재 팔로워 리스트를 수정 가능한 리스트로 복사합니다.
            val followerListCopy = _followerList.value.toMutableList()
            val followingListCopy = _followingList.value.toMutableList()

            // 3. 대상 찾기: 리스트 내에서 팔로우 버튼을 누른 특정 유저의 위치(index)를 찾습니다.
            val followerIndex = followerListCopy.indexOfFirst { it.users.id == targetUserId }
            val followingIndex = followingListCopy.indexOfFirst { it.users.id == targetUserId }

            // 4. 대상이 존재할 때만 실행합니다.
            val targetItem =
                if (followerIndex != -1) followerListCopy[followerIndex]
                else if (followingIndex != -1) followingListCopy[followingIndex]
                else return@launch

            // 5. 이전 상태 저장: 통신 실패 시 되돌리기 위해 현재 팔로우 여부를 기록해둡니다.
            val prevStatus = targetItem.isFollowing
            Log.d("FollowTest", "클릭 시점 prevStatus: $prevStatus")

            // 6. [낙관적 업데이트] 즉시 반영: 서버 응답 전, 리스트의 해당 아이템 상태를 반대로 바꿉니다.
            // true면 false로, false면 true로 즉각 변경하여 UI에 전달합니다.
            if (followerIndex != -1) {
                followerListCopy[followerIndex] = targetItem.copy(isFollowing = !prevStatus)
                _followerList.value = followerListCopy
            }
            if (followingIndex != -1) {
                followingListCopy[followingIndex] = targetItem.copy(isFollowing = !prevStatus)
                _followingList.value = followingListCopy
            }

            // 7. 서버 통신 시도: try-catch 문을 통해 실제 DB 작업을 수행합니다.
            try {
                if (!prevStatus) {
                    // 8. 이전에 팔로우 안 한 상태였다면 -> 팔로우 추가 요청
                    followUserUseCase(myId = myId, targetUserId = targetUserId).getOrThrow()
                } else {
                    Log.d("FollowTest", "언팔로우 실행")
                    // 9. 이전에 팔로우 한 상태였다면 -> 언팔로우 요청
                    unfollowUserUseCase(myId = myId, targetUserId = targetUserId)
                }
                // 성공 시: 이미 UI를 6번에서 바꿨으므로 추가 작업 없이 끝냅니다.
            } catch (e: Exception) {
                // 10. [롤백] 실패 시: 서버 통신이 실패하면 아까 저장해둔 prevStatus로 리스트를 원복합니다.
                if (followerIndex != -1) {
                    followerListCopy[followerIndex] = targetItem.copy(isFollowing = prevStatus)
                    _followerList.value = followerListCopy
                }
                if (followingIndex != -1) {
                    followingListCopy[followingIndex] = targetItem.copy(isFollowing = prevStatus)
                    _followingList.value = followingListCopy
                }
                Log.e("FollowError", "팔로우 토글 실패: ${e.message}")
                }

        }
    }

    fun checkFollowStatUs(targetUserId: String){
        Log.d(Constants.TAG, "팔로우 상태 체크 시작: $targetUserId")
        viewModelScope.launch {
            val myId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?: return@launch
            if (myId == targetUserId) return@launch

            val result = checkFollowStatusUseCase(myId = myId, targetUserId = targetUserId)

            result.onSuccess {
                Log.d(Constants.TAG, "서버 응답 결과: $it")
                _isFollowing.value = it
            }.onFailure {
                _isFollowing.value = false
            }
        }
    }
}
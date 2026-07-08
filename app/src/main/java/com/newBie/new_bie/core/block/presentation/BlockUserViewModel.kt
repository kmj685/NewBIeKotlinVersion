package com.newBie.new_bie.core.block.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newBie.new_bie.core.block.domain.entities.BlockUserEntity
import com.newBie.new_bie.core.block.domain.usecase.DeleteBlockUserCase
import com.newBie.new_bie.core.block.domain.usecase.GetBlockUserListUseCase
import com.newBie.new_bie.core.block.domain.usecase.InsertBlockUserUseCase
import com.newBie.new_bie.core.block.domain.usecase.InsertReportUserUseCase
import com.newBie.new_bie.core.managers.SupabaseManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockUserViewModel @Inject constructor(
    private val insertBlockUserUseCase: InsertBlockUserUseCase,
    private val deleteBlockUserCase: DeleteBlockUserCase,
    private val getBlockUserListUseCase: GetBlockUserListUseCase,
    private val insertReportUserUseCase: InsertReportUserUseCase
): ViewModel(){
    private val _blockedUserList = MutableStateFlow<List<BlockUserEntity>>(emptyList())
    val blockedUserList = _blockedUserList.asStateFlow()

    val showDialog = MutableStateFlow<Boolean>(false)
    private val _blockedUser = MutableStateFlow<BlockUserEntity?>(null)
    val blockedUser = _blockedUser.asStateFlow()
    private val _showReportDialog = MutableStateFlow(false)
    val showReportDialog = _showReportDialog.asStateFlow()

    init {
        fetchBlockedUserList()
    }

    fun insertBlockUser(targetId: String){
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch

            val result = insertBlockUserUseCase(
                userId = userId,
                blockedUserId = targetId
            )
            result.onSuccess {
                fetchBlockedUserList()
            }
        }
    }

    fun fetchBlockedUserList(){
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch

            val result = getBlockUserListUseCase(
                userId = userId
            )
            result.onSuccess {
                _blockedUserList.value = it
            }
        }
    }

    fun cancelBlockUser(targetId: String){
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch

            val result = deleteBlockUserCase(
                userId = userId,
                blockedUserId = targetId
            )
            result.onSuccess {
                // 로컬로 업데이트
                _blockedUserList.update { currentList ->
                    currentList.filterNot {it.blockedUserId.id == targetId} // filterNot: 내가 지정한 조건과 일치하는 항목을 제외, id가 targetID와 같은 유저를 제외한 나머지 유저들만 담아서 업데이트해라
                }
            }
        }
    }

    // 다이얼로그에 세팅할 값
    fun setDialogTargetUser(user: BlockUserEntity) {
        _blockedUser.value = user
    }

    // 유저 신고
    fun insertReportUser(reportedId: String, category: String, content: String?){
        viewModelScope.launch {
            val userId = SupabaseManager.supabase.auth.currentUserOrNull()?.id ?:return@launch

            val result = insertReportUserUseCase(
                senderId = userId,
                reportedId = reportedId,
                category = category,
                content = content
            )

            result.onSuccess {
                _showReportDialog.value = false
            }
        }
    }

    // 신고창 여는 상태 값
    fun showReportDialog(show: Boolean) {
        _showReportDialog.value = show
    }
}
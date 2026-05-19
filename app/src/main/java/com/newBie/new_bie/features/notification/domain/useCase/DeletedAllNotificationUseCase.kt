package com.newBie.new_bie.features.notification.domain.useCase

import com.newBie.new_bie.features.notification.domain.repository.NotificationsRepository
import javax.inject.Inject

// @Inject constructor를 붙여서 힐트가 이 유즈케이스를 스스로 생성할 수 있게 만듦
class DeletedAllNotificationUseCase @Inject constructor(private val repository: NotificationsRepository) {

    suspend operator fun invoke(receiverId: String): Result<Unit>{
        return try {
            val results = repository.deletedAllNotification(
                receiverId = receiverId
            )
            Result.success(results)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
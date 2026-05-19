package com.newBie.new_bie.features.notification.domain.useCase

import com.newBie.new_bie.features.notification.domain.entities.NotificationsEntity
import com.newBie.new_bie.features.notification.domain.repository.NotificationsRepository
import javax.inject.Inject

// @Inject constructor를 붙여서 힐트가 이 유즈케이스를 스스로 생성할 수 있게 만듦
class FetchNotificationsListUseCase @Inject constructor(private val repository: NotificationsRepository) {

    // operator fun invoke: 클래스를 함수처럼 호출할 수 있게 해줌
    suspend operator fun invoke(): Result<List<NotificationsEntity>>{
        return try {
            val list = repository.fetchNotifications()
            Result.success(list)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
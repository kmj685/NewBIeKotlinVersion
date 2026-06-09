package com.newBie.new_bie.features.notification.domain.di

import com.newBie.new_bie.features.notification.data.repositories.NotificationsRepositoryImpl
import com.newBie.new_bie.features.notification.domain.repository.NotificationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("UNUSED_PARAMETER") // 안 쓰는 파라미터라고 경고 주지 말라는 어노테이션
abstract class NotificationModule { //abstract class 추상클래스를 쓰는 이유: 미완성인 추상클래스를 hilt가 나중에 알아서 갖다 쓰기 떄문에
    @Binds
    @Singleton
    @Suppress("UNUSED_PARAMETER")
    abstract fun bindNotificationsRepository(
        notificationsRepositoryImpl: NotificationsRepositoryImpl // 리포지토리 구현체
    ): NotificationsRepository // 인터페이스
}
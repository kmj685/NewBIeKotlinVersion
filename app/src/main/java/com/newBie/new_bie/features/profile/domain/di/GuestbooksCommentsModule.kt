package com.newBie.new_bie.features.profile.domain.di

import com.newBie.new_bie.features.profile.data.repositories.GuestbooksCommentsRepositoryImpl
import com.newBie.new_bie.features.profile.domain.repositories.GuestbooksCommentsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GuestbooksCommentsModule {
    @Binds
    @Singleton
    abstract fun bindGuestbooksCommentsRepository(
        guestbooksCommentsRepositoryImpl: GuestbooksCommentsRepositoryImpl
    ): GuestbooksCommentsRepository
}
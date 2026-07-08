package com.newBie.new_bie.features.profile.domain.di

import com.newBie.new_bie.features.profile.data.repositories.NoticesRepositoryImpl
import com.newBie.new_bie.features.profile.domain.repositories.NoticesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NoticesModule {
    @Binds
    @Singleton
    abstract fun bindNoticesRepository(
        noticesRepositoryImpl: NoticesRepositoryImpl
    ): NoticesRepository
}
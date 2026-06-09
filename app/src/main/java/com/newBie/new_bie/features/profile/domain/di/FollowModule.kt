package com.newBie.new_bie.features.profile.domain.di

import com.newBie.new_bie.features.profile.data.repositories.ProfileRepositoryImpl
import com.newBie.new_bie.features.profile.domain.repositories.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent ::class)
@Suppress("UNUSED_PARAMETER")
abstract class FollowModule {
    @Binds
    @Singleton
    @Suppress("UNUSED_PARAMETER")
    abstract fun bindFollowRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository
}
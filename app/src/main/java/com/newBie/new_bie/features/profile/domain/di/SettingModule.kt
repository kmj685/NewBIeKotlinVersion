package com.newBie.new_bie.features.profile.domain.di

import com.newBie.new_bie.features.profile.data.repositories.SettingRepositoryImpl
import com.newBie.new_bie.features.profile.domain.repositories.SettingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent ::class)
abstract class SettingModule {
    @Binds
    @Singleton
    abstract fun bindSettingRepository(settingRepositoryImpl: SettingRepositoryImpl): SettingRepository
}
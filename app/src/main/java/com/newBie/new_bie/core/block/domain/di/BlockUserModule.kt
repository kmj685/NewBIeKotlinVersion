package com.newBie.new_bie.core.block.domain.di

import com.newBie.new_bie.core.block.data.repositories.BlockUserRepositoryImpl
import com.newBie.new_bie.core.block.domain.repositories.BlockUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("UNUSED_PARAMETER")
abstract class BlockUserModule {
    @Binds
    @Singleton
    @Suppress("UNUSED_PARAMETER")
    abstract fun bindBlockUserRepository(blockUserRepositoryImpl: BlockUserRepositoryImpl): BlockUserRepository
}
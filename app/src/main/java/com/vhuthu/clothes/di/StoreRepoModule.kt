package com.vhuthu.clothes.di

import com.vhuthu.clothes.remote.data.StoreRepo
import com.vhuthu.clothes.remote.data.StoreRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class  StoreRepoModule {

    @Binds
    @Singleton
    abstract fun bindsStoreRepo(
        storeRepoImpl: StoreRepoImpl
    ): StoreRepo
}
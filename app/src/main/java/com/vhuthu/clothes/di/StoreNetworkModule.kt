package com.vhuthu.clothes.di

import android.content.Context
import androidx.room.Room
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.vhuthu.clothes.local.dao.StoreItemDao
import com.vhuthu.clothes.local.database.ClothesDatabase
import com.vhuthu.clothes.remote.data.StoreApi
import com.vhuthu.clothes.util.Constants.BASE_URL
import com.vhuthu.clothes.util.NetworkMonitor
import com.vhuthu.clothes.util.NetworkMonitorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object StoreNetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesStoreApi(retrofit : Retrofit): StoreApi{
        return retrofit.create(StoreApi::class.java)
    }

    @Provides
    @Singleton
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): ClothesDatabase {
        return Room.databaseBuilder(
            context,
            ClothesDatabase::class.java,
            "clothes_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesStoreItemDao(database: ClothesDatabase): StoreItemDao {
        return database.storeItemDao()
    }

    @Provides
    @Singleton
    fun providesNetworkMonitor(
        networkMonitorImpl: NetworkMonitorImpl
    ): NetworkMonitor = networkMonitorImpl
}
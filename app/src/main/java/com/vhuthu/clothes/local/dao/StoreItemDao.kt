package com.vhuthu.clothes.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vhuthu.clothes.local.StoreItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreItemDao {

    @Query("SELECT * FROM store_items")
    fun getAllItems(): Flow<List<StoreItemEntity>>


    @Upsert
    suspend fun upsertAll(items: List<StoreItemEntity>)

    @Query("SELECT COUNT(*) FROM store_items")
    suspend fun count(): Int
}
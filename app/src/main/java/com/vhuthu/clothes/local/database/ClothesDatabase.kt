package com.vhuthu.clothes.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vhuthu.clothes.local.StoreItemEntity
import com.vhuthu.clothes.local.dao.StoreItemDao

@Database(entities = [StoreItemEntity::class], version = 1, exportSchema = false)
abstract class ClothesDatabase : RoomDatabase() {
    abstract fun storeItemDao(): StoreItemDao
}
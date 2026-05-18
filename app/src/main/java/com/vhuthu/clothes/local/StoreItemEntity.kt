package com.vhuthu.clothes.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_items")
data class StoreItemEntity(
    @PrimaryKey val id: Int,
    val category: String,
    val description: String,
    val image: String,
    val price: Double,
    val ratingRate: Double,
    val ratingCount: Int,
    val title: String
)

package com.vhuthu.clothes.local

import com.vhuthu.clothes.model.AllStoreItems
import com.vhuthu.clothes.model.Rating

fun StoreItemEntity.toDomain(): AllStoreItems = AllStoreItems(
    id = id,
    category = category,
    description = description,
    image = image,
    price = price,
    rating = Rating(rate = ratingRate, count = ratingCount),
    title = title
)

fun AllStoreItems.toEntity(): StoreItemEntity = StoreItemEntity(
    id = id,
    category = category,
    description = description,
    image = image,
    price = price,
    ratingRate = rating.rate,
    ratingCount = rating.count,
    title = title
)
package com.vhuthu.clothes.remote.data

import com.skydoves.sandwich.ApiResponse
import com.vhuthu.clothes.model.AllStoreItems
import com.vhuthu.clothes.model.SingleStoreItem
import kotlinx.coroutines.flow.Flow

interface StoreRepo {
    fun fetchAllStoreItems(
        onStart:() -> Unit,
        onComplete:() -> Unit,
        onError: (String?) -> Unit
    ): Flow<List<AllStoreItems>>

    fun fetchSingleStoreItem(
        id : String,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
    ): Flow<SingleStoreItem>
}
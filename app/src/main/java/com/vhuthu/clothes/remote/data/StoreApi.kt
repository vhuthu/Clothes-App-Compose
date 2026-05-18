package com.vhuthu.clothes.remote.data

import com.skydoves.sandwich.ApiResponse
import com.vhuthu.clothes.model.AllStoreItems
import com.vhuthu.clothes.model.SingleStoreItem
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApi {
    @GET("/products")
    suspend fun fetchAllStoreItems(): ApiResponse<List<AllStoreItems>>

    @GET("/products/{id}")
    suspend fun fetchSingleProductItem(
        @Path("id") id : String
    ) : ApiResponse<SingleStoreItem>
}
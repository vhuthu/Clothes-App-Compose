package com.vhuthu.clothes.remote.data

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.vhuthu.clothes.local.dao.StoreItemDao
import com.vhuthu.clothes.local.toDomain
import com.vhuthu.clothes.local.toEntity
import com.vhuthu.clothes.model.AllStoreItems
import com.vhuthu.clothes.model.SingleStoreItem
import com.vhuthu.clothes.util.NetworkMonitor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class StoreRepoImpl @Inject constructor(
    private val storeApi: StoreApi,
    private val storeItemDao: StoreItemDao,
    private val networkMonitor: NetworkMonitor,
    private val ioDispatcher: CoroutineDispatcher
) : StoreRepo {

    override fun fetchAllStoreItems(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<List<AllStoreItems>> = flow {


        val cached = storeItemDao.getAllItems().first()
        if (cached.isNotEmpty()) {
            emit(cached.map { it.toDomain() })
        }

        if (networkMonitor.isCurrentlyOnline()) {
            val response = storeApi.fetchAllStoreItems()
            response.suspendOnSuccess {
                storeItemDao.upsertAll(data.map { it.toEntity() })
                emit(data)
            }.onFailure {
                onError(message())
            }
        } else {
            if (cached.isEmpty()) {
                onError("You are offline and no cached data is available.")
            }
        }

    }.onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    override fun fetchSingleStoreItem(
        id: String,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<SingleStoreItem> = flow {
        val response = storeApi.fetchSingleProductItem(id)
        response.suspendOnSuccess {
            emit(data)
        }.onFailure {
            onError(message())
        }
    }.onCompletion { onComplete() }
        .flowOn(ioDispatcher)
}
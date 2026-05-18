package com.vhuthu.clothes.model

sealed interface StoreResult<out T> {
    data object Loading : StoreResult<Nothing>
    data class Success<T>(val data: T) : StoreResult<T>
    data class Error(val message: String) : StoreResult<Nothing>
    data class Exception(val throwable: Throwable) : StoreResult<Nothing>
}
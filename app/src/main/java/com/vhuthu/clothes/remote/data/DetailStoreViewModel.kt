package com.vhuthu.clothes.remote.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vhuthu.clothes.model.SingleStoreItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = DetailStoreViewModel.Factory::class)
class DetailStoreViewModel  @AssistedInject constructor(
    private val storeRepo: StoreRepo,
    @Assisted private val itemId: String
) : ViewModel(){

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val storeItem: StateFlow<SingleStoreItem?> = storeRepo.fetchSingleStoreItem(
        id = itemId,
        onComplete = { _isLoading.value = false },
        onError = { _errorMessage.value = it },
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    @AssistedFactory
    interface Factory {
        fun create(itemId: String): DetailStoreViewModel
    }
}
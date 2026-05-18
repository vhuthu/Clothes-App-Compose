package com.vhuthu.clothes.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vhuthu.clothes.model.AllStoreItems
import com.vhuthu.clothes.remote.data.StoreRepo
import com.vhuthu.clothes.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val storeRepo: StoreRepo,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val isOffline: StateFlow<Boolean> = networkMonitor.isOnline
        .map { !it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val fetchTrigger = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val storeItems: StateFlow<List<AllStoreItems>> = fetchTrigger
        .flatMapLatest {
            storeRepo.fetchAllStoreItems(
                onStart = { _isLoading.value = true },
                onComplete = { _isLoading.value = false },
                onError = { _errorMessage.value = it },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun retry() {
        _errorMessage.value = null
        viewModelScope.launch {
            fetchTrigger.emit(Unit)
        }
    }
}
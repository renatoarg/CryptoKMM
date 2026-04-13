package com.gmail.renatoarg.cryptokmm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.renatoarg.cryptokmm.connectivity.ConnectivityMonitor
import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin
import com.gmail.renatoarg.cryptokmm.domain.usecase.GetCryptoListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CryptoListUiState(
    val cryptoList: List<CryptoCoin> = emptyList(),
    val isLoading: Boolean = true,
)

class CryptoListViewModel(
    private val useCase: GetCryptoListUseCase,
    connectivityMonitor: ConnectivityMonitor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CryptoListUiState())
    val uiState: StateFlow<CryptoListUiState> = _uiState

    val isConnected: StateFlow<Boolean> = connectivityMonitor.isConnected

    init {
        viewModelScope.launch {
            val cached = useCase.cached()
            _uiState.value = _uiState.value.copy(cryptoList = cached)

            try {
                val fresh = useCase.refresh()
                _uiState.value = _uiState.value.copy(cryptoList = fresh, isLoading = false)
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}

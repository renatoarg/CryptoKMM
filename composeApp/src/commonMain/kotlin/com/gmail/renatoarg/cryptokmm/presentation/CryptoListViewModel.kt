package com.gmail.renatoarg.cryptokmm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin
import com.gmail.renatoarg.cryptokmm.domain.usecase.GetCryptoListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val useCase: GetCryptoListUseCase
) : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<CryptoCoin>>(emptyList())
    val cryptoList: StateFlow<List<CryptoCoin>> = _cryptoList

    init {
        viewModelScope.launch {
            _cryptoList.value = useCase()
        }
    }

}
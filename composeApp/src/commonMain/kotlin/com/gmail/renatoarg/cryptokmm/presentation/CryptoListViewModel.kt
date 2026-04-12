package com.gmail.renatoarg.cryptokmm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.renatoarg.cryptokmm.domain.usecase.GetCryptoListUseCase
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val useCase: GetCryptoListUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            useCase()
        }
    }

}
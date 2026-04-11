package com.gmail.renatoarg.cryptokmm.presentation

import androidx.lifecycle.ViewModel
import com.gmail.renatoarg.cryptokmm.domain.usecase.GetCryptoListUseCase

class CryptoListViewModel(
    useCase: GetCryptoListUseCase
) : ViewModel() {

    init {
        println("-=> viewModel: ${useCase.invoke()}")
    }

}
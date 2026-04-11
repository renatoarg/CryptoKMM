package com.gmail.renatoarg.cryptokmm.domain.usecase

import com.gmail.renatoarg.cryptokmm.domain.repository.CryptoRepository

class GetCryptoListUseCase(
    private val repository: CryptoRepository
) {

    operator fun invoke() {
        println(repository.getCryptoListFromRemote())
    }

}
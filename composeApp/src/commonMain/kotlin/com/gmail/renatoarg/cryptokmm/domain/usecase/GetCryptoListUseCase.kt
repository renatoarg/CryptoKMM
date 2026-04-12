package com.gmail.renatoarg.cryptokmm.domain.usecase

import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin
import com.gmail.renatoarg.cryptokmm.domain.repository.CryptoRepository

class GetCryptoListUseCase(
    private val repository: CryptoRepository
) {

    suspend operator fun invoke(): List<CryptoCoin> {
        return repository.fetchCryptoPrices()
    }

}
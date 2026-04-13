package com.gmail.renatoarg.cryptokmm.domain.usecase

import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin
import com.gmail.renatoarg.cryptokmm.domain.repository.CryptoRepository

class GetCryptoListUseCase(
    private val repository: CryptoRepository
) {

    suspend fun cached(): List<CryptoCoin> = repository.getCachedCryptoPrices()

    suspend fun refresh(): List<CryptoCoin> = repository.refreshCryptoPrices()
}

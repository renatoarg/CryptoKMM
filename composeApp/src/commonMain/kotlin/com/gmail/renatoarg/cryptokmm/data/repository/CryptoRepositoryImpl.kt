package com.gmail.renatoarg.cryptokmm.data.repository

import com.gmail.renatoarg.cryptokmm.data.mapper.toDomain
import com.gmail.renatoarg.cryptokmm.data.remote.CryptoRemoteDatasource
import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin
import com.gmail.renatoarg.cryptokmm.domain.repository.CryptoRepository

/**
 * [CryptoRepository] implementation that delegates to [CryptoRemoteDatasource].
 */
internal class CryptoRepositoryImpl(
    private val remoteDatasource: CryptoRemoteDatasource,
) : CryptoRepository {

    override suspend fun fetchCryptoPrices(): List<CryptoCoin> =
        remoteDatasource.fetchCryptoPrices().toDomain()
}
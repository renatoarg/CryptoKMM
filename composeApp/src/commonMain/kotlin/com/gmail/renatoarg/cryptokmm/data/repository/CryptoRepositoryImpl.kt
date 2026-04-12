package com.gmail.renatoarg.cryptokmm.data.repository

import com.gmail.renatoarg.cryptokmm.data.remote.CryptoRemoteDatasource
import com.gmail.renatoarg.cryptokmm.data.remote.dto.CryptoCoinDto
import com.gmail.renatoarg.cryptokmm.domain.repository.CryptoRepository

/**
 * [CryptoRepository] implementation that delegates to [CryptoRemoteDatasource].
 */
internal class CryptoRepositoryImpl(
    private val remoteDatasource: CryptoRemoteDatasource,
) : CryptoRepository {

    override fun getCryptoListFromRemote() = remoteDatasource.getCryptoImageUrls()

    override suspend fun fetchCryptoPrices(): List<CryptoCoinDto> =
        remoteDatasource.fetchCryptoPrices()
}
package com.gmail.renatoarg.cryptokmm.data.repository

import com.gmail.renatoarg.cryptokmm.data.remote.CryptoRemoteDatasource
import com.gmail.renatoarg.cryptokmm.domain.repository.CryptoRepository

internal class CryptoRepositoryImpl(
    private val remoteDatasource: CryptoRemoteDatasource
) : CryptoRepository {
    override fun getCryptoListFromRemote() = remoteDatasource.getCryptoImageUrls()
}
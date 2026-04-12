package com.gmail.renatoarg.cryptokmm.data.repository

import com.gmail.renatoarg.cryptokmm.data.local.CryptoLocalDatasource
import com.gmail.renatoarg.cryptokmm.data.mapper.toDomain
import com.gmail.renatoarg.cryptokmm.data.mapper.toDomainFromEntity
import com.gmail.renatoarg.cryptokmm.data.mapper.toEntity
import com.gmail.renatoarg.cryptokmm.data.remote.CryptoRemoteDatasource
import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin
import com.gmail.renatoarg.cryptokmm.domain.repository.CryptoRepository

/**
 * [CryptoRepository] implementation that delegates to [CryptoRemoteDatasource]
 * and caches results locally via [CryptoLocalDatasource].
 */
internal class CryptoRepositoryImpl(
    private val remoteDatasource: CryptoRemoteDatasource,
    private val localDatasource: CryptoLocalDatasource,
) : CryptoRepository {

    override suspend fun fetchCryptoPrices(): List<CryptoCoin> =
        try {
            val dtos = remoteDatasource.fetchCryptoPrices()
            runCatching { localDatasource.replaceAll(dtos.toEntity()) }
            dtos.toDomain()
        } catch (_: Exception) {
            localDatasource.getAll().toDomainFromEntity()
        }
}

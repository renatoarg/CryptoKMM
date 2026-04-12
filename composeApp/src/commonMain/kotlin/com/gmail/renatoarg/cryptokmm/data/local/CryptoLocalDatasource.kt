package com.gmail.renatoarg.cryptokmm.data.local

import com.gmail.renatoarg.cryptokmm.data.local.dao.CryptoCoinDao
import com.gmail.renatoarg.cryptokmm.data.local.entity.CryptoCoinEntity

internal class CryptoLocalDatasource(
    private val dao: CryptoCoinDao,
) {
    suspend fun getAll(): List<CryptoCoinEntity> = dao.getAll()

    suspend fun replaceAll(coins: List<CryptoCoinEntity>) {
        dao.replaceAll(coins)
    }
}

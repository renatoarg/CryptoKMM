package com.gmail.renatoarg.cryptokmm.data.local

import com.gmail.renatoarg.cryptokmm.data.local.dao.CryptoCoinDao
import com.gmail.renatoarg.cryptokmm.data.local.entity.CryptoCoinEntity

/**
 * In-memory fake of [CryptoCoinDao] for unit tests.
 *
 * Set [shouldFailOnWrite] to `true` to simulate write failures
 * while keeping reads functional.
 */
class FakeCryptoCoinDao : CryptoCoinDao {

    private val coins = mutableListOf<CryptoCoinEntity>()

    var shouldFailOnWrite = false

    override suspend fun insertAll(coins: List<CryptoCoinEntity>) {
        if (shouldFailOnWrite) throw RuntimeException("Fake write failure")
        this.coins.addAll(coins)
    }

    override suspend fun getAll(): List<CryptoCoinEntity> = coins.toList()

    override suspend fun deleteAll() {
        if (shouldFailOnWrite) throw RuntimeException("Fake write failure")
        coins.clear()
    }
}

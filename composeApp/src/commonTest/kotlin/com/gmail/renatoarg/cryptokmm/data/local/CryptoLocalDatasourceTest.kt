package com.gmail.renatoarg.cryptokmm.data.local

import com.gmail.renatoarg.cryptokmm.data.local.entity.CryptoCoinEntity
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Unit tests for [CryptoLocalDatasource] using [FakeCryptoCoinDao]. */
class CryptoLocalDatasourceTest {

    private fun sampleEntities() = listOf(
        CryptoCoinEntity("bitcoin", "btc", "Bitcoin", "img", 67000.0, 1_300_000_000_000, 1, 2.5),
        CryptoCoinEntity("ethereum", "eth", "Ethereum", "img", 3500.0, 420_000_000_000, 2, -1.2),
    )

    @Test
    fun getAll_returnsEmptyListInitially() = runTest {
        val datasource = CryptoLocalDatasource(FakeCryptoCoinDao())

        val result = datasource.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    fun replaceAll_storesCoins() = runTest {
        val datasource = CryptoLocalDatasource(FakeCryptoCoinDao())

        datasource.replaceAll(sampleEntities())

        val result = datasource.getAll()
        assertEquals(2, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals("ethereum", result[1].id)
    }

    @Test
    fun replaceAll_replacesExistingData() = runTest {
        val datasource = CryptoLocalDatasource(FakeCryptoCoinDao())
        datasource.replaceAll(sampleEntities())

        val newCoins = listOf(
            CryptoCoinEntity("solana", "sol", "Solana", "img", 150.0, 65_000_000_000, 5, 5.0),
        )
        datasource.replaceAll(newCoins)

        val result = datasource.getAll()
        assertEquals(1, result.size)
        assertEquals("solana", result[0].id)
    }

    @Test
    fun getAll_returnsSnapshotNotLiveReference() = runTest {
        val datasource = CryptoLocalDatasource(FakeCryptoCoinDao())
        datasource.replaceAll(sampleEntities())

        val snapshot = datasource.getAll()
        datasource.replaceAll(emptyList())

        assertEquals(2, snapshot.size, "Snapshot should not be affected by later replaceAll")
    }
}

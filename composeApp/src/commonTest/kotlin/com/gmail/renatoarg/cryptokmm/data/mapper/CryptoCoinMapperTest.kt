package com.gmail.renatoarg.cryptokmm.data.mapper

import com.gmail.renatoarg.cryptokmm.data.remote.dto.CryptoCoinDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CryptoCoinMapperTest {

    @Test
    fun toDomain_mapsSingleDto() {
        val dto = CryptoCoinDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "https://example.com/btc.png",
            currentPrice = 67000.0,
            marketCap = 1_300_000_000_000,
            marketCapRank = 1,
            priceChangePercentage24h = 2.5,
        )

        val result = dto.toDomain()

        assertEquals("bitcoin", result.id)
        assertEquals("btc", result.symbol)
        assertEquals("Bitcoin", result.name)
        assertEquals("https://example.com/btc.png", result.image)
        assertEquals(67000.0, result.currentPrice)
        assertEquals(1_300_000_000_000, result.marketCap)
        assertEquals(1, result.marketCapRank)
        assertEquals(2.5, result.priceChangePercentage24h)
    }

    @Test
    fun toDomain_preservesNullableFields() {
        val dto = CryptoCoinDto(
            id = "newcoin",
            symbol = "new",
            name = "NewCoin",
            image = "https://example.com/new.png",
            currentPrice = 0.01,
            marketCap = 1000,
            marketCapRank = null,
            priceChangePercentage24h = null,
        )

        val result = dto.toDomain()

        assertNull(result.marketCapRank)
        assertNull(result.priceChangePercentage24h)
    }

    @Test
    fun toDomain_mapsList() {
        val dtos = listOf(
            CryptoCoinDto(
                id = "bitcoin",
                symbol = "btc",
                name = "Bitcoin",
                image = "https://example.com/btc.png",
                currentPrice = 67000.0,
                marketCap = 1_300_000_000_000,
                marketCapRank = 1,
                priceChangePercentage24h = 2.5,
            ),
            CryptoCoinDto(
                id = "ethereum",
                symbol = "eth",
                name = "Ethereum",
                image = "https://example.com/eth.png",
                currentPrice = 3500.0,
                marketCap = 420_000_000_000,
                marketCapRank = 2,
                priceChangePercentage24h = -1.2,
            ),
        )

        val result = dtos.toDomain()

        assertEquals(2, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals("ethereum", result[1].id)
    }
}

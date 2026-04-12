package com.gmail.renatoarg.cryptokmm.data.mapper

import com.gmail.renatoarg.cryptokmm.data.local.entity.CryptoCoinEntity
import com.gmail.renatoarg.cryptokmm.data.remote.dto.CryptoCoinDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CryptoCoinEntityMapperTest {

    // region DTO → Entity

    @Test
    fun toEntity_mapsSingleDto() {
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

        val result = dto.toEntity()

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
    fun toEntity_preservesNullableFields() {
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

        val result = dto.toEntity()

        assertNull(result.marketCapRank)
        assertNull(result.priceChangePercentage24h)
    }

    @Test
    fun toEntity_mapsList() {
        val dtos = listOf(
            CryptoCoinDto("bitcoin", "btc", "Bitcoin", "img", 67000.0, 1_300_000_000_000, 1, 2.5),
            CryptoCoinDto("ethereum", "eth", "Ethereum", "img", 3500.0, 420_000_000_000, 2, -1.2),
        )

        val result = dtos.toEntity()

        assertEquals(2, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals("ethereum", result[1].id)
    }

    // endregion

    // region Entity → Domain

    @Test
    fun toDomain_mapsSingleEntity() {
        val entity = CryptoCoinEntity(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "https://example.com/btc.png",
            currentPrice = 67000.0,
            marketCap = 1_300_000_000_000,
            marketCapRank = 1,
            priceChangePercentage24h = 2.5,
        )

        val result = entity.toDomain()

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
        val entity = CryptoCoinEntity(
            id = "newcoin",
            symbol = "new",
            name = "NewCoin",
            image = "https://example.com/new.png",
            currentPrice = 0.01,
            marketCap = 1000,
            marketCapRank = null,
            priceChangePercentage24h = null,
        )

        val result = entity.toDomain()

        assertNull(result.marketCapRank)
        assertNull(result.priceChangePercentage24h)
    }

    @Test
    fun toDomainFromEntity_mapsList() {
        val entities = listOf(
            CryptoCoinEntity("bitcoin", "btc", "Bitcoin", "img", 67000.0, 1_300_000_000_000, 1, 2.5),
            CryptoCoinEntity("ethereum", "eth", "Ethereum", "img", 3500.0, 420_000_000_000, 2, -1.2),
        )

        val result = entities.toDomainFromEntity()

        assertEquals(2, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals("ethereum", result[1].id)
    }

    // endregion
}

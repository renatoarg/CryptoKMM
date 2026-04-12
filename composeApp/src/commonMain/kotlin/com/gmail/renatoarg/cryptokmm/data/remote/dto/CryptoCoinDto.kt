package com.gmail.renatoarg.cryptokmm.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO representing a cryptocurrency from the CoinGecko `/coins/markets` endpoint.
 *
 * Nullable fields ([marketCapRank], [priceChangePercentage24h]) may be absent
 * for newly listed or low-volume coins.
 */
@Serializable
data class CryptoCoinDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    @SerialName("current_price") val currentPrice: Double,
    @SerialName("market_cap") val marketCap: Long,
    @SerialName("market_cap_rank") val marketCapRank: Int?,
    @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double?,
)

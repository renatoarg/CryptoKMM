package com.gmail.renatoarg.cryptokmm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crypto_coins")
data class CryptoCoinEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val marketCap: Long,
    val marketCapRank: Int?,
    val priceChangePercentage24h: Double?,
)

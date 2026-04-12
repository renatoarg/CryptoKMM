package com.gmail.renatoarg.cryptokmm.data.mapper

import com.gmail.renatoarg.cryptokmm.data.remote.dto.CryptoCoinDto
import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin

fun CryptoCoinDto.toDomain(): CryptoCoin = CryptoCoin(
    id = id,
    symbol = symbol,
    name = name,
    image = image,
    currentPrice = currentPrice,
    marketCap = marketCap,
    marketCapRank = marketCapRank,
    priceChangePercentage24h = priceChangePercentage24h,
)

fun List<CryptoCoinDto>.toDomain(): List<CryptoCoin> = map { it.toDomain() }

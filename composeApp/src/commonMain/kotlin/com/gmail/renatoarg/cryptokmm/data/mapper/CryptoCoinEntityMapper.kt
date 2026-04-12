package com.gmail.renatoarg.cryptokmm.data.mapper

import com.gmail.renatoarg.cryptokmm.data.local.entity.CryptoCoinEntity
import com.gmail.renatoarg.cryptokmm.data.remote.dto.CryptoCoinDto
import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin

fun CryptoCoinEntity.toDomain(): CryptoCoin = CryptoCoin(
    id = id,
    symbol = symbol,
    name = name,
    image = image,
    currentPrice = currentPrice,
    marketCap = marketCap,
    marketCapRank = marketCapRank,
    priceChangePercentage24h = priceChangePercentage24h,
)

fun List<CryptoCoinEntity>.toDomainFromEntity(): List<CryptoCoin> = map { it.toDomain() }

fun CryptoCoinDto.toEntity(): CryptoCoinEntity = CryptoCoinEntity(
    id = id,
    symbol = symbol,
    name = name,
    image = image,
    currentPrice = currentPrice,
    marketCap = marketCap,
    marketCapRank = marketCapRank,
    priceChangePercentage24h = priceChangePercentage24h,
)

fun List<CryptoCoinDto>.toEntity(): List<CryptoCoinEntity> = map { it.toEntity() }

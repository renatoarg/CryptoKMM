package com.gmail.renatoarg.cryptokmm.domain.repository

import com.gmail.renatoarg.cryptokmm.data.remote.dto.CryptoCoinDto

/** Repository contract for cryptocurrency data operations. */
interface CryptoRepository {

    /** Returns hardcoded JSON with crypto image URLs. */
    fun getCryptoListFromRemote(): String

    /** Fetches live cryptocurrency market prices. */
    suspend fun fetchCryptoPrices(): List<CryptoCoinDto>
}
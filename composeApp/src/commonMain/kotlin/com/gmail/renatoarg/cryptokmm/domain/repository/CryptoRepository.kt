package com.gmail.renatoarg.cryptokmm.domain.repository

import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin

/** Repository contract for cryptocurrency data operations. */
interface CryptoRepository {

    /** Fetches live cryptocurrency market prices. */
    suspend fun fetchCryptoPrices(): List<CryptoCoin>
}
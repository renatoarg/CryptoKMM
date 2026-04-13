package com.gmail.renatoarg.cryptokmm.domain.repository

import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin

/** Repository contract for cryptocurrency data operations. */
interface CryptoRepository {

    /** Returns locally cached cryptocurrency prices, or an empty list if the cache is empty. */
    suspend fun getCachedCryptoPrices(): List<CryptoCoin>

    /** Fetches live prices from the remote API, saves them to cache, and returns them.
     *  Throws on network failure. */
    suspend fun refreshCryptoPrices(): List<CryptoCoin>
}

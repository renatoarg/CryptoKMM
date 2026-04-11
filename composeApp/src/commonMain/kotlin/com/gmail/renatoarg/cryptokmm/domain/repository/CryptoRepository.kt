package com.gmail.renatoarg.cryptokmm.domain.repository

interface CryptoRepository {
    fun getCryptoListFromRemote(): String
}
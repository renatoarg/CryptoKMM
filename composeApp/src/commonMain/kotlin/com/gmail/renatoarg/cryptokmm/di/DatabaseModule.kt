package com.gmail.renatoarg.cryptokmm.di

import com.gmail.renatoarg.cryptokmm.data.local.CryptoDatabase
import com.gmail.renatoarg.cryptokmm.data.local.createCryptoDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { createCryptoDatabase(get()) }
    single { get<CryptoDatabase>().cryptoCoinDao() }
}

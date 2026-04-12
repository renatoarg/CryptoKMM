package com.gmail.renatoarg.cryptokmm.di

import androidx.room.Room
import com.gmail.renatoarg.cryptokmm.data.local.CryptoDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformDatabaseModule = module {
    single {
        Room.databaseBuilder<CryptoDatabase>(
            context = androidContext(),
            name = androidContext().getDatabasePath("crypto_kmm.db").absolutePath,
        )
    }
}

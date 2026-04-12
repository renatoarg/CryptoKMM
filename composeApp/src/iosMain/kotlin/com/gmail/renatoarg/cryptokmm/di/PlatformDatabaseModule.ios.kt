package com.gmail.renatoarg.cryptokmm.di

import androidx.room.Room
import com.gmail.renatoarg.cryptokmm.data.local.CryptoDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory

@OptIn(ExperimentalForeignApi::class)
actual val platformDatabaseModule = module {
    single {
        val dbDir = NSHomeDirectory() + "/Documents/databases"
        NSFileManager.defaultManager.createDirectoryAtPath(
            path = dbDir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null,
        )
        Room.databaseBuilder<CryptoDatabase>(
            name = "$dbDir/crypto_kmm.db",
        )
    }
}

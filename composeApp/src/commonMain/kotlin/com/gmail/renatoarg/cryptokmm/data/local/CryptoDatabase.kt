package com.gmail.renatoarg.cryptokmm.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.gmail.renatoarg.cryptokmm.data.local.dao.CryptoCoinDao
import com.gmail.renatoarg.cryptokmm.data.local.entity.CryptoCoinEntity

@Database(entities = [CryptoCoinEntity::class], version = 1)
@ConstructedBy(CryptoDatabaseConstructor::class)
abstract class CryptoDatabase : RoomDatabase() {
    abstract fun cryptoCoinDao(): CryptoCoinDao
}

@Suppress("KotlinNoActualForExpect")
expect object CryptoDatabaseConstructor : RoomDatabaseConstructor<CryptoDatabase>

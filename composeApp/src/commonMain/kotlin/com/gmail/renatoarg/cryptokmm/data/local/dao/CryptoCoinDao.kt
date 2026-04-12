package com.gmail.renatoarg.cryptokmm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gmail.renatoarg.cryptokmm.data.local.entity.CryptoCoinEntity

@Dao
interface CryptoCoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<CryptoCoinEntity>)

    @Query("SELECT * FROM crypto_coins")
    suspend fun getAll(): List<CryptoCoinEntity>

    @Query("DELETE FROM crypto_coins")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(coins: List<CryptoCoinEntity>) {
        deleteAll()
        insertAll(coins)
    }
}

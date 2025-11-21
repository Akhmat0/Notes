package ru.smak.lazyelems.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(entity = Card::class)
    suspend fun addCard(card: Card)

    @Delete(entity = Card::class)
    suspend fun deleteCard(card: Card)

    @Query("SELECT * FROM card ORDER BY title, text")
    fun getAllCards(): Flow<List<Card>>

    @Query("SELECT * FROM card WHERE id=:id")
    fun getCardById(id: Int): Flow<Card?>
}
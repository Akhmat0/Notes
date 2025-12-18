package ru.smak.lazyelems.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCard(card: Card)

    @Update
    suspend fun updateCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)

    // Сначала приоритет (0=HIGH,1=NORMAL,2=LOW), внутри — по дате (новые выше)
    @Query(
        """
        SELECT * FROM card
        ORDER BY priority ASC, last_modified DESC
        """
    )
    fun getAllCards(): Flow<List<Card>>

    @Query("SELECT * FROM card WHERE id = :id")
    fun getCardById(id: Int): Flow<Card?>
}

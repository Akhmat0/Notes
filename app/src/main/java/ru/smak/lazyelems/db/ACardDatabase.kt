package ru.smak.lazyelems.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Card::class],
    version = 2,          // было 1 → стало 2
    exportSchema = false
)
abstract class ACardDatabase : RoomDatabase() {
    abstract fun cardsDao(): CardDao
}

object CardDatabase {
    private lateinit var db: CardDao

    fun getDb(context: Context): CardDao {
        if (!::db.isInitialized) {
            db = Room.databaseBuilder(
                context.applicationContext,
                ACardDatabase::class.java,
                "db_cards"
            )
                .fallbackToDestructiveMigration() // для задания проще всего
                .build()
                .cardsDao()
        }
        return db
    }
}

package ru.smak.lazyelems.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Card::class], version = 1, exportSchema = false)
abstract class ACardDatabase : RoomDatabase(){
    abstract fun cardsDao(): CardDao
}

object CardDatabase{
    private lateinit var db: CardDao

    fun getDb(context: Context): CardDao {
        if (!::db.isInitialized){
            db = Room.databaseBuilder(
                context,
                ACardDatabase::class.java,
                "db_cards"
            ).build()
                .cardsDao()
        }
        return db
    }
}
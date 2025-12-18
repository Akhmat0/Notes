package ru.smak.lazyelems.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card")
data class Card(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    // Заголовок может быть пустым
    @ColumnInfo(name = "title")
    var title: String? = null,

    // Текст может быть пустым
    @ColumnInfo(name = "text")
    var text: String? = null,

    // Приоритет: 0 – высокий, 1 – нормальный, 2 – низкий
    @ColumnInfo(name = "priority")
    var priority: Int = 1,

    // Время последнего редактирования (мс с эпохи)
    @ColumnInfo(name = "last_modified")
    var lastModified: Long = System.currentTimeMillis()
)

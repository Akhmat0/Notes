package ru.smak.lazyelems.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.smak.lazyelems.db.Card
import ru.smak.lazyelems.db.CardDatabase

class MainViewModel(app: Application) : AndroidViewModel(app) {

    val values = mutableStateListOf<Card>()

    var showDialog by mutableStateOf(false)
    var editableCard by mutableStateOf<Card?>(null)

    var page: Pages by mutableStateOf(Pages.MAIN)

    init {
        viewModelScope.launch {
            CardDatabase.getDb(getApplication()).getAllCards().collect { list ->
                values.apply {
                    clear()
                    addAll(list)
                }
            }
        }
    }




    fun addValue(title: String, text: String, priority: Int) {

        if (title.isBlank() && text.isBlank()) return

        val card = Card(
            title = title.ifBlank { null },
            text = text.ifBlank { null },
            priority = priority,
            lastModified = System.currentTimeMillis()
        )

        viewModelScope.launch {
            CardDatabase.getDb(getApplication()).addCard(card)
        }
    }


    fun updateValue(card: Card, title: String, text: String, priority: Int) {
        if (title.isBlank() && text.isBlank()) return

        val updated = card.copy(
            title = title.ifBlank { null },
            text = text.ifBlank { null },
            priority = priority,
            lastModified = System.currentTimeMillis()
        )

        viewModelScope.launch {
            CardDatabase.getDb(getApplication()).updateCard(updated)
        }
    }

    fun deleteValue(card: Card) {
        viewModelScope.launch {
            CardDatabase.getDb(getApplication()).deleteCard(card)
        }
    }
}

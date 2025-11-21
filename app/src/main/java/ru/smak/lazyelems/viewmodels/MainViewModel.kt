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

    var page: Pages by mutableStateOf(Pages.MAIN)

    init {
        viewModelScope.launch {
            CardDatabase.getDb(getApplication()).getAllCards().collect { it ->
                values.apply {
                    clear()
                    addAll(it)
                }
            }
        }
    }

    fun toList(){
        page = Pages.LIST
    }

    fun back(){
        page = Pages.MAIN
    }

    fun addValue(title: String, text: String){
        viewModelScope.launch {
            CardDatabase.getDb(getApplication()).addCard(
                Card(title = title, text = text)
            )
        }
    }
}
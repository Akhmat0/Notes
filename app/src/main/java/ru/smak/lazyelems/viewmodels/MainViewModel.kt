package ru.smak.lazyelems.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val values = mutableStateListOf<Int>()

    var page: Pages by mutableStateOf(Pages.MAIN)

    fun toList(){
        page = Pages.LIST
    }

    fun back(){
        page = Pages.MAIN
    }

    fun addValue(){
        if (values.isEmpty()) values.add(1)
        else values.add(values.last() + 1)
    }
}
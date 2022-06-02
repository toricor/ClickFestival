package com.github.toricor.clickfestival.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    private val _clickCount = MutableLiveData<Int>(0)
    val clickCount: LiveData<Int>
        get() = _clickCount

    fun onButtonClick() {
        _clickCount.value = _clickCount.value?.plus(1)
    }
}
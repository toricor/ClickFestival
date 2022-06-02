package com.github.toricor.clickfestival.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.min

private val CLICK_BUZZ_PATTERN = longArrayOf(100, 100)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)
private val INITIAL_TEXT_SIZE = 72
private val MAX_TEXT_SIZE = 72

class GameViewModel: ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CLICK(CLICK_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    private val _clickCount = MutableLiveData<Int>(0)
    val clickCount: LiveData<Int>
        get() = _clickCount

    private val _textSize = MutableLiveData<Int>(INITIAL_TEXT_SIZE)
    val textSize: LiveData<Int>
        get() = _textSize

    private val _textRotation = MutableLiveData<Int>()
    val textRotation: LiveData<Int>
        get() = _textRotation

    private val _eventGameFinish = MutableLiveData<Boolean>(false)

    private val _eventBuzz = MutableLiveData<BuzzType>(BuzzType.NO_BUZZ)
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    fun onButtonClick() {
        _clickCount.value = _clickCount.value?.plus(1)

        val clickCount = clickCount.value!!

        // some effects that children enjoy
        _eventBuzz.value = BuzzType.CLICK

        if (clickCount < 24) {
            return
        }

        if (24 < clickCount) {
            _textSize.value = calcTextSize(clickCount)
        }

        if (60 < clickCount) {
            _textRotation.value = calcTextRotation(clickCount)
        }
    }

    private fun calcTextRotation(v: Int): Int {
        return (v - 60) * 5
    }

    private fun calcTextSize(v: Int): Int {
        return v * 5
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }
}
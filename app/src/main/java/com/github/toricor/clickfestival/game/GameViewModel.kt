package com.github.toricor.clickfestival.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.min

private val CLICK_BUZZ_PATTERN = longArrayOf(100, 100)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)
private const val INITIAL_TEXT_SIZE = 72
private const val MAX_TEXT_SIZE = 120 * 5

private const val FIRST_COUNT_THRESHOLD = 12
private const val SECOND_COUNT_THRESHOLD = 48
private const val THIRD_COUNT_THRESHOLD = 120

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

    private val _textRotation = MutableLiveData<Int>(0)
    val textRotation: LiveData<Int>
        get() = _textRotation

    private val _clickButtonRotation = MutableLiveData<Int>(0)
    val clickButtonRotation: LiveData<Int>
        get() = _clickButtonRotation

    private val _eventGameFinish = MutableLiveData<Boolean>(false)

    private val _eventBuzz = MutableLiveData<BuzzType>(BuzzType.NO_BUZZ)
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    fun onButtonClick() {
        _clickCount.value = _clickCount.value?.plus(1)

        val clickCount = clickCount.value!!

        // some effects that children enjoy
        _eventBuzz.value = BuzzType.CLICK

        if (clickCount < FIRST_COUNT_THRESHOLD) {
            return
        }

        if (FIRST_COUNT_THRESHOLD <= clickCount) {
            _textSize.value = calcTextSize(clickCount)
        }

        if (SECOND_COUNT_THRESHOLD <= clickCount) {
            _textRotation.value = calcTextRotation(clickCount)
        }

        if (THIRD_COUNT_THRESHOLD <= clickCount) {
            _clickButtonRotation.value = calcTextRotation(clickCount)
        }
    }

    private fun calcTextRotation(v: Int): Int {
        return (v - SECOND_COUNT_THRESHOLD) * 5
    }

    private fun calcTextSize(v: Int): Int {
        return min(v * 5, MAX_TEXT_SIZE)
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }
}
package com.github.toricor.clickfestival.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlin.math.min
import kotlin.math.max

private val CLICK_BUZZ_PATTERN = longArrayOf(100, 100)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val NO_BUZZ_PATTERN = longArrayOf(0)
private const val INITIAL_TEXT_SIZE = 72
private const val MAX_TEXT_SIZE = 120 * 5

private const val FIRST_COUNT_THRESHOLD = 12
private const val SECOND_COUNT_THRESHOLD = 48
private const val THIRD_COUNT_THRESHOLD = 120

class GameViewModel: ViewModel() {

    companion object {
        // These represent different important times in the game, such as game length.

        // This is when the game is over
        private const val DONE = 0L

        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L
        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L

        // This is the total time of the game
        private const val COUNTDOWN_TIME = 40000L

    }

    enum class BuzzType(val pattern: LongArray) {
        CLICK(CLICK_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
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
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val _eventBuzz = MutableLiveData<BuzzType>(BuzzType.NO_BUZZ)
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    private val _translationY = MutableLiveData<Int>()
    val translationY: LiveData<Int>
        get() = _translationY

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private val timer: CountDownTimer

    init {
        _clickCount.value = 0

        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
                _eventBuzz.value = BuzzType.GAME_OVER
            }
        }

        timer.start()
    }

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
            _translationY.value = calcTranslationY(clickCount)
        }
    }

    private fun calcTextRotation(v: Int): Int {
        return (v - SECOND_COUNT_THRESHOLD) * 5
    }

    private fun calcTextSize(v: Int): Int {
        return min(v * 5, MAX_TEXT_SIZE)
    }

    private fun calcTranslationY(v: Int): Int {
        return max(0, (v - THIRD_COUNT_THRESHOLD) * 3)
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
        _clickCount.value = 0
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }
}
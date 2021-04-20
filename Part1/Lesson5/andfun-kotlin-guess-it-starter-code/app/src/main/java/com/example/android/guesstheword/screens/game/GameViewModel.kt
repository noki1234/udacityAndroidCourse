package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.Buzz

class GameViewModel : ViewModel(){

    companion object {
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private var COUNTDOWN_TIME = 60000L
    }

    private val timer: CountDownTimer

    private var _time = MutableLiveData<Long>()
    val time : LiveData<Long>
        get() = _time

    val currentTimeString = Transformations.map(time) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The current word
    private var _word = MutableLiveData<String>()
    val word : LiveData<String>
    get() = _word

    private var _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
    get() = _eventGameFinished

    // The current score
     private var _score = MutableLiveData<Int>()
     val score: LiveData<Int>
        get() = _score

    private var _buzzType = MutableLiveData<Buzz.BuzzType>()
    val buzzType : LiveData<Buzz.BuzzType>
    get() = _buzzType

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel created!")
        _eventGameFinished.value = false
        resetList()
        nextWord()
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                Log.i("HA: ", millisUntilFinished.toString())
                // TODO implement what should happen each tick of the timer
                _time.value = (millisUntilFinished+1000L).div(1000L)
                if (_time.value!! <= 5)
                    _buzzType.value = Buzz.BuzzType.COUNTDOWN_PANIC
            }

            override fun onFinish() {
                _eventGameFinished.value = true
                _buzzType.value = Buzz.BuzzType.GAME_OVER
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
                resetList()
          //  _eventGameFinished.value = true
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (_score.value)?.minus( 1)
        nextWord()
    }

    fun onCorrect() {
//        _buzzType.value = Buzz.BuzzType.CORRECT
        _score.value = (_score.value)?.plus( 1)
        nextWord()
    }

    fun onGameFinishedComplete(){
        _eventGameFinished.value = false
    }

    fun setTime(milliSeconds: Long){
        COUNTDOWN_TIME = milliSeconds
        timer.onFinish()
    }


}

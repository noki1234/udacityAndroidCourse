package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {


    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
    get() = _score

    private var _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
    get() = _eventPlayAgain


    init {
        _score.value = finalScore
        _eventPlayAgain.value = false
        Log.i("ScoreViewModel", "Final score: $finalScore")
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun setEventPlayAgain(value: Boolean){
        _eventPlayAgain.value = value
    }
}
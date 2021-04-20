package com.example.android.guesstheword

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService

class Buzz (context: Context){
    var context = context

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(longArrayOf(100, 100, 100, 100, 100, 100)),
        GAME_OVER(longArrayOf(0, 2000)),
        COUNTDOWN_PANIC( longArrayOf(0, 200)),
        NO_BUZZ(longArrayOf(0));
    }

    fun buzz(buzzType: BuzzType) {
        val buzzer = context?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(buzzType.pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(buzzType.pattern, -1)
            }
        }
    }

}
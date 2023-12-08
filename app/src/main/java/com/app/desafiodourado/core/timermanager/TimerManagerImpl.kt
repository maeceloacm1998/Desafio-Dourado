package com.app.desafiodourado.core.timermanager

import android.os.CountDownTimer
import com.app.desafiodourado.core.utils.DateUtils.EMPTY_HOUR
import com.app.desafiodourado.core.utils.DateUtils.INTERVAL
import com.app.desafiodourado.core.utils.DateUtils.formatHour
import com.app.desafiodourado.core.utils.DateUtils.getHours
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class TimerManagerImpl : TimerManager {
    private var countDownTimer: CountDownTimer? = null
    private val countdown = MutableStateFlow("")

    override fun observeCountdown(): Flow<String> = countdown

    private fun setting() {
        val currentHour = Calendar.getInstance()

        val finishHour = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 24)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val initialHour = finishHour.timeInMillis - currentHour.timeInMillis

        countDownTimer = object : CountDownTimer(initialHour, INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val (hour, minutes, seconds) = getHours(millisUntilFinished)
                countdown.update { formatHour(hour, minutes, seconds) }
            }

            override fun onFinish() {
                countdown.update { EMPTY_HOUR }
            }
        }
    }

    override fun startCountDown() {
        setting()
        countDownTimer?.start()
    }

    override fun stopCountDown() {
        countDownTimer = null
    }

    override fun restartCountDown() {
        stopCountDown()
        setting()
        startCountDown()
    }
}
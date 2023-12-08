package com.app.desafiodourado.core.timermanager

import kotlinx.coroutines.flow.Flow

interface TimerManager {
    fun observeCountdown(): Flow<String>
    fun startCountDown()
    fun stopCountDown()
    fun restartCountDown()
}
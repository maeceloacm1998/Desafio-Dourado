package com.app.desafiodourado.feature.home.domain

import com.app.desafiodourado.core.timermanager.TimerManager

class StartCountDownUseCase(private val timerManager: TimerManager) {
    operator fun invoke() {
        timerManager.startCountDown()
    }
}
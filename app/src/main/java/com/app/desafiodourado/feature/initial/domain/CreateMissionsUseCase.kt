package com.app.desafiodourado.feature.initial.domain

import android.annotation.SuppressLint
import android.provider.Settings
import com.app.desafiodourado.feature.initial.data.InitialRepository

class CreateMissionsUseCase(
    private val context: android.content.Context,
    private val repository: InitialRepository
) {
    suspend operator fun invoke(): Result<Boolean> =
        repository.createMissions(getDeviceId())

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}
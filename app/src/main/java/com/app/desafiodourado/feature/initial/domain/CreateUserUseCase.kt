package com.app.desafiodourado.feature.initial.domain

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.app.desafiodourado.commons.mock.missions
import com.app.desafiodourado.core.firebase.models.UserModel
import com.app.desafiodourado.core.utils.DateUtils
import com.app.desafiodourado.feature.initial.data.InitialRepository

class CreateUserUseCase(
    private val context: Context,
    private val repository: InitialRepository
) {
    suspend operator fun invoke(userName: String): Result<Boolean> {
        val randomMissions = missions.shuffled().take(4)

        val userModel = UserModel(
            id = getDeviceId(),
            name = userName,
            quantityCoins = 0,
            currentMissions = randomMissions,
            lastUpdateMissions = DateUtils.getCurrentDate(),
            dtCreate = DateUtils.getCurrentDate(),
            yearCreate = DateUtils.getCurrentYear()
        )
        return repository.createUserInFirebase(userModel)
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}
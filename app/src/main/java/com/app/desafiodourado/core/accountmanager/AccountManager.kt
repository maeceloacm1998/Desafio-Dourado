package com.app.desafiodourado.core.accountmanager

import com.app.desafiodourado.core.firebase.models.UserModel
import com.app.desafiodourado.feature.home.ui.model.Missions
import kotlinx.coroutines.flow.Flow

interface AccountManager {
    suspend fun createUser(user: UserModel): Result<Boolean>
    suspend fun updateUserInfo(user: UserModel): Result<Boolean>
    suspend fun updateCurrentMissions()
    suspend fun updateCoins()
    fun startCountDownTimer()
    fun observeCoins(): Flow<Int>
    fun observeMissions(): Flow<List<Missions.MissionsModel>>
    fun observeCountdown(): Flow<String>
    fun getUserLogged(): UserModel
    fun getQuantityCoins(): Int
    fun postUserLogged(user: UserModel)
    fun getUserId(): String
    fun userIsLogged(): Boolean
}
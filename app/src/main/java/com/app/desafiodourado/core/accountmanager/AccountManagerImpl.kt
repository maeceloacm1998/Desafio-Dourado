package com.app.desafiodourado.core.accountmanager

import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants
import com.app.desafiodourado.core.firebase.models.UserModel
import com.google.gson.Gson
import com.app.desafiodourado.core.sharedpreferences.SharedPreferencesBuilder
import com.app.desafiodourado.core.timermanager.TimerManager
import com.app.desafiodourado.feature.home.ui.model.Missions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AccountManagerImpl(
    private val client: FirebaseClient,
    private val sharedPreferences: SharedPreferencesBuilder,
    private val timerManager: TimerManager
) : AccountManager {
    private val coins = MutableStateFlow(0)
    private val missions = MutableStateFlow<List<Missions.MissionsModel>>(mutableListOf())

    override suspend fun createUser(user: UserModel): Result<Boolean> {
        coins.update { user.quantityCoins }
        missions.update { user.currentMissions }
        sharedPreferences.putString(USER_ACCOUNT, Gson().toJson(user))
        return client.setSpecificDocument(FirebaseConstants.Collections.USERS, user.id, user)
    }

    override suspend fun updateUserInfo(user: UserModel): Result<Boolean> {
        coins.update { user.quantityCoins }
        missions.update { user.currentMissions }
        sharedPreferences.putString(USER_ACCOUNT, Gson().toJson(user))
        return client.setSpecificDocument(FirebaseConstants.Collections.USERS, user.id, user)
    }

    override fun observeCoins(): Flow<Int> = coins

    override fun observeMissions(): Flow<List<Missions.MissionsModel>> = missions
    override fun observeCountdown(): Flow<String> = timerManager.observeCountdown()

    override fun updateCoins() {
        val user = getUserLogged()
        coins.update { user.quantityCoins }
    }

    override suspend fun updateCurrentMissions() {
        val id = getUserId()
        client.getSpecificDocument(FirebaseConstants.Collections.USERS, id)
            .onSuccess {
                val user = it.toObject(UserModel::class.java)
                updateUserInfo(checkNotNull(user))
            }
    }

    override fun startCountDownTimer() {
        timerManager.startCountDown()
    }

    override fun getUserLogged(): UserModel {
        return sharedPreferences.getString(USER_ACCOUNT, "").run {
            Gson().fromJson(this, UserModel::class.java)
        }
    }

    override fun postUserLogged(user: UserModel) {
        sharedPreferences.run {
            putBoolean(USER_LOGGED, true)
            sharedPreferences.putString(USER_ACCOUNT, Gson().toJson(user))
        }
    }

    override fun getQuantityCoins(): Int {
        return sharedPreferences.getString(USER_ACCOUNT, "").run {
            Gson().fromJson(this, UserModel::class.java)
        }.quantityCoins
    }

    override fun getUserId(): String {
        return sharedPreferences.getString(USER_ACCOUNT, "").run {
            Gson().fromJson(this, UserModel::class.java)
        }.id
    }

    override fun userIsLogged(): Boolean {
        return sharedPreferences.getBoolean(USER_LOGGED, false)
    }

    companion object {
        private const val USER_LOGGED = "user_logged"
        private const val USER_ACCOUNT = "user_account"
    }
}
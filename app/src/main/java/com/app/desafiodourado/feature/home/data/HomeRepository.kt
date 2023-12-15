package com.app.desafiodourado.feature.home.data

import com.app.desafiodourado.core.firebase.models.UserModel
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.Missions
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getChallengers(): Result<Challenger>
    suspend fun getRandomMissions(): Result<List<Missions.MissionsModel>>
    suspend fun updateCurrentMissions()
    suspend fun createNewMissions()
    suspend fun updateCoins()
    fun observeCoins(): Flow<Int>
    fun observeMissions(): Flow<List<Missions.MissionsModel>>
    fun getUser(): UserModel
    fun getCoins(): Int
}
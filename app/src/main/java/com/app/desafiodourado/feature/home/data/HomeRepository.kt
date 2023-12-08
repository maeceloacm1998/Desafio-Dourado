package com.app.desafiodourado.feature.home.data

import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.Missions

interface HomeRepository {
    suspend fun getChallengers(): Result<Challenger>
    suspend fun completeChallenger(challengerList: List<Challenger.Card>): Result<Challenger>
    suspend fun getRandomMissions(): Result<List<Missions.MissionsModel>>
    fun getCoins(): Int
}
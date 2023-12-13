package com.app.desafiodourado.commons

import com.app.desafiodourado.commons.mock.challenger
import com.app.desafiodourado.commons.mock.challengerWithCardComplete
import com.app.desafiodourado.commons.mock.missions
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.Missions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class FakeHomeRepositoryImpl {
    suspend fun getChallengers(): Result<Challenger> {
        return withContext(Dispatchers.IO) {
            val challenger = challenger
            if (challenger == null) {
                Result.Error(IllegalArgumentException("Challenger not found"))
            } else {
                Result.Success(challenger)
            }
        }
    }

    suspend fun completeChallenger(challengerList: List<Challenger.Card>): Result<Challenger> {
        return withContext(Dispatchers.IO) {
            val challengerWithCardComplete = challengerWithCardComplete
            if (challengerWithCardComplete == null) {
                Result.Error(IllegalArgumentException("Challenger not found"))
            } else {
                Result.Success(challengerWithCardComplete)
            }
        }
    }

    suspend fun getRandomMissions(): Result<List<Missions.MissionsModel>> {
        return withContext(Dispatchers.IO) {
            val missions = missions.shuffled().take(4)
            Result.Success(missions)
        }
    }

    fun getCoins(): Int {
        return 200
    }
}
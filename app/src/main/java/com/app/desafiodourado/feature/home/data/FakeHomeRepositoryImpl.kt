package com.app.desafiodourado.feature.home.data

import com.app.desafiodourado.core.firebase.models.UserModel
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.Missions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
class FakeHomeRepositoryImpl : HomeRepository {
    override suspend fun getChallengers(): Result<Challenger> {
        return withContext(Dispatchers.IO) {
            val challenger = challenger
            if (challenger == null) {
                Result.Error(IllegalArgumentException("Challenger not found"))
            } else {
                Result.Success(challenger)
            }
        }
    }

    override suspend fun completeChallenger(challengerList: List<Challenger.Card>): Result<Challenger> {
        return withContext(Dispatchers.IO) {
            val challengerWithCardComplete = challengerWithCardComplete
            if (challengerWithCardComplete == null) {
                Result.Error(IllegalArgumentException("Challenger not found"))
            } else {
                Result.Success(challengerWithCardComplete)
            }
        }
    }

    override suspend fun getRandomMissions(): Result<List<Missions.MissionsModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCurrentMissions() {
        TODO("Not yet implemented")
    }

    override suspend fun createNewMissions() {
        TODO("Not yet implemented")
    }

    override suspend fun updateCoins() {
        TODO("Not yet implemented")
    }

    override fun observeCoins(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun observeMissions(): Flow<List<Missions.MissionsModel>> {
        TODO("Not yet implemented")
    }

    override fun getUser(): UserModel {
        TODO("Not yet implemented")
    }

    override fun getCoins(): Int {
        return 200
    }
}
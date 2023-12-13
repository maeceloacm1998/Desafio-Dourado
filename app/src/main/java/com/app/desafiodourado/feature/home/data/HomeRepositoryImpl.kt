package com.app.desafiodourado.feature.home.data

import com.app.desafiodourado.core.accountmanager.AccountManager
import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants
import com.app.desafiodourado.core.firebase.models.UserModel
import com.app.desafiodourado.core.utils.DateUtils.getCurrentDate
import com.app.desafiodourado.feature.home.ui.model.Challenger
import kotlinx.coroutines.Dispatchers
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Missions
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    private val client: FirebaseClient,
    private val accountManager: AccountManager
) : HomeRepository {
    override suspend fun getChallengers(): Result<Challenger> {
        val id = accountManager.getUserId()
        val request = withContext(Dispatchers.IO) {
            client.getSpecificDocument(
                collectionPath = FirebaseConstants.Collections.CHALLENGERS,
                documentPath = id
            )
        }

        if (request.isFailure) {
            return Result.Error(IllegalArgumentException("Error"))
        }

        val response = request.getOrThrow().toObject(Challenger::class.java)
        return Result.Success(checkNotNull(response))
    }

    override suspend fun completeChallenger(challengerList: List<Challenger.Card>): Result<Challenger> {
        val id = accountManager.getUserId()
        val challenger = Challenger(challengerList)
        val request = withContext(Dispatchers.IO) {
            client.setSpecificDocument(
                collectionPath = FirebaseConstants.Collections.CHALLENGERS,
                documentPath = id,
                data = challenger
            )
        }

        if (request.isFailure) {
            return Result.Error(IllegalArgumentException("Error"))
        }

        return Result.Success(challenger)
    }

    override suspend fun getRandomMissions(): Result<List<Missions.MissionsModel>> {
        val id = accountManager.getUserId()
        val request = withContext(Dispatchers.IO) {
            client.getSpecificDocument(
                collectionPath = FirebaseConstants.Collections.MISSIONS,
                documentPath = id
            )
        }

        if (request.isFailure) {
            return Result.Error(IllegalArgumentException("Error get missions"))
        }

        val result = request.getOrThrow().toObject(Missions::class.java)

        return Result.Success(checkNotNull(result?.missions?.shuffled()?.take(4)))
    }

    override suspend fun updateCurrentMissions() {
        return accountManager.updateCurrentMissions()
    }

    override suspend fun createNewMissions() {
        when(val request = getRandomMissions()) {
            is Result.Success -> {
                updateWithNewMissionsLocal(
                    missions = request.data,
                    lastUpdateCurrentMissions = getCurrentDate()
                )
            }
            else -> {}
        }
    }

    private suspend fun updateWithNewMissionsLocal(
        missions: List<Missions.MissionsModel>,
        lastUpdateCurrentMissions: String
    ) {
        val user = accountManager.getUserLogged().copy(
            currentMissions = missions,
            lastUpdateMissions = lastUpdateCurrentMissions
        )
        accountManager.updateUserInfo(user)
    }

    override fun getUser(): UserModel = accountManager.getUserLogged()
    override fun getCoins(): Int = accountManager.getQuantityCoins()

}
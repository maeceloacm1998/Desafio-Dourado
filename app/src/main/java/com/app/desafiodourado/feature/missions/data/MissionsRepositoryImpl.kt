package com.app.desafiodourado.feature.missions.data

import com.app.desafiodourado.core.accountmanager.AccountManager
import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Missions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MissionsRepositoryImpl(
    private val client: FirebaseClient,
    private val accountManager: AccountManager
) : MissionsRepository {
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
}
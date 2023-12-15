package com.app.desafiodourado.feature.challengerdetails.data

import com.app.desafiodourado.core.accountmanager.AccountManager
import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Challenger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailsRepositoryImpl(
    private val client: FirebaseClient,
    private val accountManager: AccountManager
) : DetailsRepository {
    override suspend fun completeChallenger(challengerList: List<Challenger.Card>): Result<Boolean> {
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

        return Result.Success(true)
    }
}
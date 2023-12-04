package com.app.desafiodourado.feature.home.data

import com.app.desafiodourado.core.accountManager.AccountManager
import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants
import com.app.desafiodourado.feature.home.ui.model.Challenger
import kotlinx.coroutines.Dispatchers
import com.app.desafiodourado.core.utils.Result
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

    override fun getCoins(): Int = accountManager.getQuantityCoins()
}
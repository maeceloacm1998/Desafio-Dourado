package com.app.desafiodourado.feature.initial.data

import com.app.desafiodourado.core.accountmanager.AccountManager
import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants
import com.app.desafiodourado.core.firebase.models.UserModel
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.Missions

class InitialRepositoryImpl(
    private val accountManager: AccountManager,
    private val client: FirebaseClient,
) : InitialRepository {
    override suspend fun createUserInFirebase(user: UserModel): Result<Boolean> {
        accountManager.postUserLogged(user)
        return accountManager.createUser(user)
    }

    override suspend fun createChallengers(id: String): Result<Boolean> {
        return client.setSpecificDocument(
            FirebaseConstants.Collections.CHALLENGERS,
            id,
            Challenger(challengers)
        )
    }

    override suspend fun createMissions(id: String): Result<Boolean> {
        return client.setSpecificDocument(
            FirebaseConstants.Collections.MISSIONS,
            id,
            Missions(missions)
        )
    }
}
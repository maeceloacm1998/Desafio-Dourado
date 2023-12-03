package com.app.desafiodourado.feature.details.data

import com.app.desafiodourado.core.accountManager.AccountManager
import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants
import com.app.desafiodourado.core.firebase.FirebaseConstants.Collections.CHALLENGERS
import com.app.desafiodourado.feature.home.ui.model.Challenger

class DetailsRepositoryImpl(
    private val client: FirebaseClient,
    private val accountManager: AccountManager
) : DetailsRepository {
    override suspend fun updateChallengers(challenger: Challenger): Result<Boolean> {
        val id = accountManager.getUserId()
        return client.setSpecificDocument(
            collectionPath = CHALLENGERS,
            documentPath = id,
            data = challenger
        )
    }
}
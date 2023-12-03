package com.app.desafiodourado.feature.home.data

import com.app.desafiodourado.core.accountManager.AccountManager
import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants.Collections.CHALLENGERS
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Math.abs
import kotlin.random.Random

class HomeRepositoryImpl(
    private val client: FirebaseClient,
    private val accountManager: AccountManager
) : HomeRepository {
    override suspend fun getChallengers(): Result<DocumentSnapshot> {
        val id = accountManager.getUserId()
        return client.getSpecificDocument(collectionPath = CHALLENGERS, documentPath = id)
    }

    override suspend fun setChallengers(cardList: Challenger) {
        val id = accountManager.getUserId()
        val newCard = cardList.challengers.toMutableList()
        newCard.map { it.id = kotlin.math.abs(Random.nextLong()).toString() }
        client.setSpecificDocument(collectionPath = CHALLENGERS, documentPath = id, data = newCard)
    }

    override fun getCoins(): Int = accountManager.getQuantityCoins()

}
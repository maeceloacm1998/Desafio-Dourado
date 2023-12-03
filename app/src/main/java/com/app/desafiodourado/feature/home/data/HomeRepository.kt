package com.app.desafiodourado.feature.home.data

import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.google.firebase.firestore.DocumentSnapshot

interface HomeRepository {
    suspend fun getChallengers(): Result<DocumentSnapshot>
    suspend fun setChallengers(cardList: Challenger)
    fun getCoins(): Int
}
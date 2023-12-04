package com.app.desafiodourado.feature.home.data

import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Challenger

interface HomeRepository {
    suspend fun getChallengers(): Result<Challenger>
    fun getCoins(): Int
}
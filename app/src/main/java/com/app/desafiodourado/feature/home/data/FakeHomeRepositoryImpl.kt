package com.app.desafiodourado.feature.home.data

import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Challenger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FakeHomeRepositoryImpl: HomeRepository {
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

    override fun getCoins(): Int {
        TODO("Not yet implemented")
    }
}
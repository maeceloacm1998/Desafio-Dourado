package com.app.desafiodourado.feature.details.data

import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Challenger

interface DetailsRepository {
    suspend fun completeChallenger(challengerList: List<Challenger.Card>): Result<Boolean>
}
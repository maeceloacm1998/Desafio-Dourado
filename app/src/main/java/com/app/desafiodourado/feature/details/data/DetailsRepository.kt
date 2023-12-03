package com.app.desafiodourado.feature.details.data

import com.app.desafiodourado.feature.home.ui.model.Challenger

interface DetailsRepository {
    suspend fun updateChallengers(challenger: Challenger): Result<Boolean>
}
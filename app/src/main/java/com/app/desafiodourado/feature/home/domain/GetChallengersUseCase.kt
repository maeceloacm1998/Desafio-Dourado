package com.app.desafiodourado.feature.home.domain;

import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.data.HomeRepository
import com.app.desafiodourado.feature.home.ui.model.Challenger

class GetChallengersUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(): Result<Challenger> = homeRepository.getChallengers()
}

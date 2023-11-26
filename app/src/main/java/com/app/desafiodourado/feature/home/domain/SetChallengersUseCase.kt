package com.app.desafiodourado.feature.home.domain

import com.app.desafiodourado.feature.home.data.HomeRepository
import com.app.desafiodourado.feature.home.ui.model.Challenger

class SetChallengersUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(cardList: Challenger) {
        repository.setChallengers(cardList)
    }
}
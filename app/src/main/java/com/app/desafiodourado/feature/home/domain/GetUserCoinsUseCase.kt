package com.app.desafiodourado.feature.home.domain

import com.app.desafiodourado.feature.home.data.HomeRepository

class GetUserCoinsUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Int = homeRepository.getCoins()
}
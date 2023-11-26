package com.app.desafiodourado.feature.home.domain

import com.app.desafiodourado.feature.home.data.HomeRepository

class GetCoinsUseCase(private val repository: HomeRepository) {
    operator fun invoke(): Int = repository.getCoins()
}
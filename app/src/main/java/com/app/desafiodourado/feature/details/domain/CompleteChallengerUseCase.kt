package com.app.desafiodourado.feature.details.domain

import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.details.data.DetailsRepository
import com.app.desafiodourado.feature.home.ui.model.Challenger

class CompleteChallengerUseCase(
    private val detailsRepository: DetailsRepository,
    private val updateQuantityCoinsUseCase: UpdateQuantityCoinsUseCase
) {
    suspend operator fun invoke(
        challengerList: MutableList<Challenger.Card>,
        challengerSelected: Challenger.Card,
        userCoins: Int
    ): Result<Challenger.Card> {
        val existChallenger = challengerList.contains(challengerSelected)

        if (existChallenger) {
            val challengerIndex = challengerList.indexOf(challengerSelected)
            val challengerItem = challengerList[challengerIndex].copy(complete = true)
            challengerList[challengerIndex] = challengerItem

            return when (detailsRepository.completeChallenger(challengerList)) {
                is Result.Success -> {
                    val newQuantityCoins = userCoins - challengerSelected.value
                    updateQuantityCoinsUseCase(newQuantityCoins)
                    Result.Success(challengerItem)
                }

                is Result.Error -> {
                    Result.Error(Exception())
                }
            }
        } else {
            return Result.Error(Exception())
        }
    }
}
package com.app.desafiodourado.feature.home.domain

import com.app.desafiodourado.feature.home.data.HomeRepository
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.Challenger.Card

class UpdateChallengersUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(challengerList: List<Card>, challenger: Card): Result<Boolean> {
        val newChallengerList = challengerList.toMutableList()
        val index = getChallengerIndex(challengerList, challenger)
        newChallengerList[index] = challengerList[index].copy(complete = true)

        return repository.updateChallengers(Challenger(newChallengerList))
    }

    private fun getChallengerIndex(challengerList: List<Card>, challenger: Card): Int {
        return challengerList.indexOf(challenger)
    }
}
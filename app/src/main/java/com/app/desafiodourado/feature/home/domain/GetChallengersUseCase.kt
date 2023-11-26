package com.app.desafiodourado.feature.home.domain

import com.app.desafiodourado.feature.home.data.HomeRepository
import com.google.firebase.firestore.DocumentSnapshot

class GetChallengersUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(): Result<DocumentSnapshot> = repository.getChallengers()
}
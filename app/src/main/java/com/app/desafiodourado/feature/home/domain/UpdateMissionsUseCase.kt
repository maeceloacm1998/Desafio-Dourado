package com.app.desafiodourado.feature.home.domain

import com.app.desafiodourado.core.utils.DateUtils.compareDates
import com.app.desafiodourado.core.utils.DateUtils.getCurrentDate
import com.app.desafiodourado.feature.home.data.HomeRepository

class UpdateMissionsUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() {
        val lastUpdateMissionDate = homeRepository.getUser().lastUpdateMissions
        if (compareDates(lastUpdateMissionDate, getCurrentDate())) {
            homeRepository.createNewMissions()
        } else {
            homeRepository.updateCurrentMissions()
        }
    }
}
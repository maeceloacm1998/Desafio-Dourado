package com.app.desafiodourado.feature.missions.data

import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Missions

interface MissionsRepository {
    suspend fun getRandomMissions(): Result<List<Missions.MissionsModel>>
}
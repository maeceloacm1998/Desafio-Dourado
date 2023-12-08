package com.app.desafiodourado.core.firebase.models

import com.app.desafiodourado.feature.home.ui.model.Missions

data class UserModel(
    val id: String = "",
    val name: String = "",
    val quantityCoins: Int = 0,
    val currentMissions: List<Missions.MissionsModel> = mutableListOf(),
    val lastUpdateMissions: String = ""
)

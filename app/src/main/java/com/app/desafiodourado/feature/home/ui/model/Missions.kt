package com.app.desafiodourado.feature.home.ui.model

data class Missions(
    val missions: List<MissionsModel>
) {
    data class MissionsModel(
        var title: String = "",
        var coinValue: Int = 0,
        var isChecked: Boolean = false,
    )
}

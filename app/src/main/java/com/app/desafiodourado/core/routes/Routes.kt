package com.app.desafiodourado.core.routes

import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.google.gson.Gson

sealed class Routes(val route: String) {
    object Initial : Routes("initial")
    object Home : Routes("home")
    object Details : Routes("details/{challengerId}") {
        fun createRoute(challengerId: String) = "details/$challengerId"
    }
}
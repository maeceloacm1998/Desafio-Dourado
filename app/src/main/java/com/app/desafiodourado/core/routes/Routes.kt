package com.app.desafiodourado.core.routes

sealed class Routes(val route: String) {
    object Initial : Routes("initial")
    object Home : Routes("home")
    object Feedback : Routes("feedback")
}
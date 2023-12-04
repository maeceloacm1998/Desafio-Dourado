package com.app.desafiodourado.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.desafiodourado.core.routes.Routes
import com.app.desafiodourado.feature.home.ui.HomeRoute
import com.app.desafiodourado.feature.initial.ui.InitialScreen

@Composable
fun DesafioDouradoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    userIsLogged: Boolean,
    startDestination: String = Routes.Initial.route,
) {
    NavHost(
        navController = navController,
        startDestination = if (userIsLogged) Routes.Home.route else startDestination,
        modifier = modifier
    ) {
        composable(route = Routes.Initial.route) {
            InitialScreen(navController)
        }

        composable(route = Routes.Home.route) {
            HomeRoute()
        }
    }
}
package com.app.desafiodourado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.desafiodourado.core.accountManager.AccountManager
import com.app.desafiodourado.core.accountManager.AccountManagerModule
import com.app.desafiodourado.core.firebase.FirebaseModule
import com.app.desafiodourado.core.routes.Routes
import com.app.desafiodourado.core.sharedPreferences.SharedPreferencesModule
import com.app.desafiodourado.feature.home.ui.HomeScreen
import com.app.desafiodourado.feature.initial.data.di.InitialModule
import com.app.desafiodourado.feature.initial.ui.InitialScreen
import com.app.desafiodourado.ui.theme.DesafioDouradoTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class MainActivity : ComponentActivity(), KoinComponent {
    private val accountManager: AccountManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        koinStarted()

        setContent {
            DesafioDouradoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationRoute(accountManager)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }

    private fun koinStarted() {
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(
                listOf(
                    FirebaseModule.modules,
                    SharedPreferencesModule.modules,
                    AccountManagerModule.modules,
                    InitialModule.modules,
                )
            )
        }
    }

    @Composable
    private fun NavigationRoute(accountManager: AccountManager) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = if (accountManager.userIsLogged()) Routes.Home.route else Routes.Initial.route
        ) {
            composable(Routes.Initial.route) { InitialScreen(navController) }
            composable(Routes.Home.route) { HomeScreen(navController) }
        }
    }
}
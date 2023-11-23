package com.app.desafiodourado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.desafiodourado.core.firebase.FirebaseModule
import com.app.desafiodourado.core.routes.Routes
import com.app.desafiodourado.feature.initial.data.di.InitialModule
import com.app.desafiodourado.feature.initial.ui.InitialScreen
import com.app.desafiodourado.ui.theme.DesafioDouradoTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        koinStarted()

        setContent {
            DesafioDouradoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationRoute()
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
                    InitialModule.modules,
                    FirebaseModule.modules
                )
            )
        }
    }

    @Composable
    private fun NavigationRoute() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Routes.Initial.route) {
            composable(Routes.Initial.route) { InitialScreen(navController) }
        }
    }
}
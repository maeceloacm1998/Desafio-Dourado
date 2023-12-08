package com.app.desafiodourado.feature

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.core.accountmanager.AccountManager
import com.app.desafiodourado.theme.DesafioDouradoTheme

@Composable
fun DesafioDouradoApp(accountManager: AccountManager) {
    val navController = rememberNavController()

    DesafioDouradoTheme {
        Background {
            DesafioDouradoNavGraph(
                navController = navController,
                userIsLogged = accountManager.userIsLogged()
            )
        }
    }
}
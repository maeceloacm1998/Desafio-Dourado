package com.app.desafiodourado.core.accountmanager

import org.koin.dsl.module

object AccountManagerModule {
    val modules = module {
        single<AccountManager> {
            AccountManagerImpl(
                client = get(),
                sharedPreferences = get(),
                timerManager = get()
            )
        }
    }
}
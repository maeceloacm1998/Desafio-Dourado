package com.app.desafiodourado.core.accountManager

import org.koin.dsl.module

object AccountManagerModule {
    val modules = module {
        single<AccountManager> {
            AccountManagerImpl(
                client = get(),
                sharedPreferences = get()
            )
        }
    }
}
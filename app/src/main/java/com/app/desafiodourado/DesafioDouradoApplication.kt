package com.app.desafiodourado

import android.app.Application
import com.app.desafiodourado.core.accountManager.AccountManagerModule
import com.app.desafiodourado.core.firebase.FirebaseModule
import com.app.desafiodourado.core.sharedPreferences.SharedPreferencesModule
import com.app.desafiodourado.feature.home.data.di.HomeModule
import com.app.desafiodourado.feature.initial.data.di.InitialModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DesafioDouradoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DesafioDouradoApplication)
            modules(
                listOf(
                    FirebaseModule.modules,
                    SharedPreferencesModule.modules,
                    AccountManagerModule.modules,
                    InitialModule.modules,
                    HomeModule.modules
                )
            )
        }
    }
}
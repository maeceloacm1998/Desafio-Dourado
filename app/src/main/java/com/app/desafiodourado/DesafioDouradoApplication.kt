package com.app.desafiodourado

import android.app.Application
import com.app.desafiodourado.core.accountmanager.AccountManagerModule
import com.app.desafiodourado.core.firebase.FirebaseModule
import com.app.desafiodourado.core.sharedpreferences.SharedPreferencesModule
import com.app.desafiodourado.core.timermanager.TimerManagerModule
import com.app.desafiodourado.feature.challengerdetails.data.di.DetailsModule
import com.app.desafiodourado.feature.home.data.di.HomeModule
import com.app.desafiodourado.feature.initial.data.di.InitialModule
import com.app.desafiodourado.feature.missions.data.di.MissionsModule
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
                    TimerManagerModule.modules,
                    AccountManagerModule.modules,
                    InitialModule.modules,
                    HomeModule.modules,
                    MissionsModule.modules,
                    DetailsModule.modules
                )
            )
        }
    }
}
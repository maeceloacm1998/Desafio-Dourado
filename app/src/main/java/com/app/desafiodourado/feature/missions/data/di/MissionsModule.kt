package com.app.desafiodourado.feature.missions.data.di

import com.app.desafiodourado.feature.missions.data.MissionsRepository
import com.app.desafiodourado.feature.missions.data.MissionsRepositoryImpl
import com.app.desafiodourado.feature.missions.ui.MissionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MissionsModule {
    val modules = module {
        factory<MissionsRepository> {
            MissionsRepositoryImpl(
                client = get(),
                accountManager = get()
            )
        }
        viewModel {
            MissionsViewModel(accountManager = get())
        }
    }
}
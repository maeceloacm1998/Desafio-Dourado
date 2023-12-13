package com.app.desafiodourado.feature.home.data.di

import com.app.desafiodourado.feature.home.data.HomeRepository
import com.app.desafiodourado.feature.home.data.HomeRepositoryImpl
import com.app.desafiodourado.feature.home.domain.StartCountDownUseCase
import com.app.desafiodourado.feature.home.domain.UpdateMissionsUseCase
import com.app.desafiodourado.feature.home.ui.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModule {
    val modules = module {
        factory<HomeRepository> { HomeRepositoryImpl(client = get(), accountManager = get()) }
        factory { UpdateMissionsUseCase(homeRepository = get()) }
        factory { StartCountDownUseCase(timerManager = get()) }
        viewModel {
            HomeViewModel(
                homeRepository = get(),
                accountManager = get(),
                updateMissionsUseCase = get(),
                startCountDownUseCase = get()
            )
        }
    }
}
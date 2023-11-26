package com.app.desafiodourado.feature.home.data.di

import com.app.desafiodourado.feature.home.data.HomeRepository
import com.app.desafiodourado.feature.home.data.HomeRepositoryImpl
import com.app.desafiodourado.feature.home.domain.GetChallengersUseCase
import com.app.desafiodourado.feature.home.domain.GetCoinsUseCase
import com.app.desafiodourado.feature.home.domain.SetChallengersUseCase
import com.app.desafiodourado.feature.home.ui.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModule {
    val modules = module {
        factory<HomeRepository> { HomeRepositoryImpl(client = get(), accountManager = get()) }
        factory { SetChallengersUseCase(repository = get()) }
        factory { GetChallengersUseCase(repository = get()) }
        factory { GetCoinsUseCase(repository = get()) }
        viewModel {
            HomeViewModel(
                getChallengersUseCase = get(),
                setChallengersUseCase = get(),
                getCoinsUseCase = get()
            )
        }
    }
}
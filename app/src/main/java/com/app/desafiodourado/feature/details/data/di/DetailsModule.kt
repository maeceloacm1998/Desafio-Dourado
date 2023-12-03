package com.app.desafiodourado.feature.details.data.di

import com.app.desafiodourado.feature.details.data.DetailsRepository
import com.app.desafiodourado.feature.details.data.DetailsRepositoryImpl
import com.app.desafiodourado.feature.details.domain.UpdateChallengersUseCase
import com.app.desafiodourado.feature.details.ui.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DetailsModule {
    val modules = module {
        factory<DetailsRepository> { DetailsRepositoryImpl(client = get(), accountManager = get()) }
        factory { UpdateChallengersUseCase(repository = get()) }
        viewModel {
            DetailsViewModel(
                updateChallengersUseCase = get(),
                getChallengersUseCase = get()
            )
        }
    }
}
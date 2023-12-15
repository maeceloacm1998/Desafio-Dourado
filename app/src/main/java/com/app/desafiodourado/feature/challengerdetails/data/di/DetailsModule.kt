package com.app.desafiodourado.feature.challengerdetails.data.di

import com.app.desafiodourado.feature.challengerdetails.data.DetailsRepository
import com.app.desafiodourado.feature.challengerdetails.data.DetailsRepositoryImpl
import com.app.desafiodourado.feature.challengerdetails.domain.CompleteChallengerUseCase
import com.app.desafiodourado.feature.challengerdetails.ui.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DetailsModule {
    val modules = module {
        factory<DetailsRepository> { DetailsRepositoryImpl(client = get(), accountManager = get()) }
        factory {
            CompleteChallengerUseCase(
                detailsRepository = get(),
                updateQuantityCoinsUseCase = get()
            )
        }
        viewModel {
            DetailsViewModel(completeChallengerUseCase = get())
        }
    }
}
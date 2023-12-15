package com.app.desafiodourado.feature.details.data.di

import com.app.desafiodourado.feature.details.data.DetailsRepository
import com.app.desafiodourado.feature.details.data.DetailsRepositoryImpl
import com.app.desafiodourado.feature.details.domain.CompleteChallengerUseCase
import com.app.desafiodourado.feature.details.domain.UpdateQuantityCoinsUseCase
import com.app.desafiodourado.feature.details.ui.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DetailsModule {
    val modules = module {
        factory<DetailsRepository> { DetailsRepositoryImpl(client = get(), accountManager = get()) }
        factory { UpdateQuantityCoinsUseCase(accountManager = get()) }
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
package com.app.desafiodourado.feature.initial.data.di

import com.app.desafiodourado.feature.initial.data.InitialRepository
import com.app.desafiodourado.feature.initial.data.InitialRepositoryImpl
import com.app.desafiodourado.feature.initial.domain.CreateUserUseCase
import com.app.desafiodourado.feature.initial.ui.InitialViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object InitialModule {
    val modules = module {
        factory<InitialRepository> { InitialRepositoryImpl(client = get()) }
        factory { CreateUserUseCase(context = androidContext(), repository = get()) }
        viewModel { InitialViewModel(createUserUseCase = get()) }
    }
}
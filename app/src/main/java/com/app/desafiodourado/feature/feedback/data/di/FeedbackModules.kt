package com.app.desafiodourado.feature.feedback.data.di

import com.app.desafiodourado.feature.feedback.data.FeedbackRepository
import com.app.desafiodourado.feature.feedback.data.FeedbackRepositoryImpl
import com.app.desafiodourado.feature.feedback.domain.CreateFeedbackUseCase
import com.app.desafiodourado.feature.feedback.domain.GetFeedbackTypesUseCase
import com.app.desafiodourado.feature.feedback.ui.FeedbackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object FeedbackModules {
    val modules = module {
        factory<FeedbackRepository> {
            FeedbackRepositoryImpl(
                firebaseClient = get()
            )
        }

        factory { GetFeedbackTypesUseCase(feedbackRepository = get()) }
        factory { CreateFeedbackUseCase(feedbackRepository = get()) }

        viewModel {
            FeedbackViewModel(
                getFeedbackTypesUseCase = get(),
                createFeedbackUseCase = get()
            )
        }
    }
}
package com.app.desafiodourado.core.timermanager

import org.koin.dsl.module

object TimerManagerModule {
    val modules = module {
        single<TimerManager> { TimerManagerImpl() }
    }
}
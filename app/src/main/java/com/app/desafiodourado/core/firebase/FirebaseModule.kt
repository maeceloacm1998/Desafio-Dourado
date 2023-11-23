package com.app.desafiodourado.core.firebase

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

object FirebaseModule {
    val modules = module {
        single { provideFirebase() }
        single<FirebaseClient> { FirebaseClientImpl(get()) }
    }

    private fun provideFirebase(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
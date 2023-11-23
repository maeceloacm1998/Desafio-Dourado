package com.app.desafiodourado.feature.initial.data

import com.app.desafiodourado.core.firebase.models.UserModel

interface InitialRepository {
    suspend fun createUserInFirebase(user: UserModel): Result<Boolean>
}
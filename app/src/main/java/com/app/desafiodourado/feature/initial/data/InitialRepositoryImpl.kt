package com.app.desafiodourado.feature.initial.data

import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants
import com.app.desafiodourado.core.firebase.models.UserModel

class InitialRepositoryImpl(private val client: FirebaseClient) : InitialRepository {
    override suspend fun createUserInFirebase(user: UserModel): Result<Boolean> {
        return client.setSpecificDocument(FirebaseConstants.Collections.USERS, user.id, user)
    }

}
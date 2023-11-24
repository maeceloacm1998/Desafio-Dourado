package com.app.desafiodourado.feature.initial.data

import com.app.desafiodourado.core.accountManager.AccountManager
import com.app.desafiodourado.core.firebase.models.UserModel

class InitialRepositoryImpl(private val accountManager: AccountManager) : InitialRepository {
    override suspend fun createUserInFirebase(user: UserModel): Result<Boolean> {
        accountManager.postUserLogged(user)
        return accountManager.createUser(user)
    }
}
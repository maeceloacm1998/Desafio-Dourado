package com.app.desafiodourado.core.accountManager

import com.app.desafiodourado.core.firebase.models.UserModel

interface AccountManager {
    suspend fun createUser(user: UserModel): Result<Boolean>
    suspend fun updateUserInfo(user: UserModel): Result<Boolean>
    fun postUserLogged(user: UserModel)
    fun getQuantityCoins(): Int
    fun getUserId(): String
    fun userIsLogged(): Boolean
}
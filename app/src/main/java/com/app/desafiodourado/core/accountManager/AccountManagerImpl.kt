package com.app.desafiodourado.core.accountManager

import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants
import com.app.desafiodourado.core.firebase.models.UserModel
import com.google.gson.Gson
import com.app.desafiodourado.core.sharedPreferences.SharedPreferencesBuilder

class AccountManagerImpl(
    private val client: FirebaseClient,
    private val sharedPreferences: SharedPreferencesBuilder
) : AccountManager {
    override suspend fun createUser(user: UserModel): Result<Boolean> =
        client.setSpecificDocument(FirebaseConstants.Collections.USERS, user.id, user)

    override suspend fun updateUserInfo(user: UserModel): Result<Boolean> =
        client.setSpecificDocument(FirebaseConstants.Collections.USERS, user.id, user)

    override fun getUserLogged(): UserModel {
        return sharedPreferences.getString(USER_ACCOUNT, "").run {
            Gson().fromJson(this, UserModel::class.java)
        }
    }

    override fun postUserLogged(user: UserModel) {
        sharedPreferences.run {
            putBoolean(USER_LOGGED, true)
            sharedPreferences.putString(USER_ACCOUNT, Gson().toJson(user))
        }
    }

    override fun getQuantityCoins(): Int {
        return sharedPreferences.getString(USER_ACCOUNT, "").run {
            Gson().fromJson(this, UserModel::class.java)
        }.quantityCoins
    }

    override fun getUserId(): String {
        return sharedPreferences.getString(USER_ACCOUNT, "").run {
            Gson().fromJson(this, UserModel::class.java)
        }.id
    }

    override fun userIsLogged(): Boolean {
        return sharedPreferences.getBoolean(USER_LOGGED, false)
    }

    companion object {
        private const val USER_LOGGED = "user_logged"
        private const val USER_ACCOUNT = "user_account"
    }
}
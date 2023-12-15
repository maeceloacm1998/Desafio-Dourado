package com.app.desafiodourado.feature.details.domain

import com.app.desafiodourado.core.accountmanager.AccountManager

class UpdateQuantityCoinsUseCase(private val accountManager: AccountManager) {
    suspend operator fun invoke(quantityCoins: Int): Result<Boolean> {
        val user = accountManager.getUserLogged().copy(quantityCoins = quantityCoins)
        return accountManager.updateUserInfo(user)
    }
}
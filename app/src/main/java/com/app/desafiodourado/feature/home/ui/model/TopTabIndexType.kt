package com.app.desafiodourado.feature.home.ui.model

enum class TopTabIndexType {
    PENDING,
    COMPLETED;

    companion object {
        private const val FIRST_TAB = 0

        fun fromValue(index: Int): TopTabIndexType {
            return when (index) {
                FIRST_TAB -> PENDING
                else -> COMPLETED
            }
        }
    }
}
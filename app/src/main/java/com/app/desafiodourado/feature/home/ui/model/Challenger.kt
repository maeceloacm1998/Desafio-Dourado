package com.app.desafiodourado.feature.home.ui.model

data class Challenger(
    val challengers: List<Card> = mutableListOf()
) {
    data class Card(
        val image: String = "",
        val completeImage: String = "",
        val type: String = "NORMAL",
        val details: String = "",
        val complete: Boolean = false,
        val value: Int = 0,
        val award: String = "",
        val awardImage: String = ""
    )
}
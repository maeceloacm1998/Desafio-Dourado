package com.app.desafiodourado.feature.home.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Challenger(
    val challengers: List<Card> = mutableListOf()
) : Parcelable {
    @Parcelize
    data class Card(
        var id: String = "",
        val image: String = "",
        val completeImage: String = "",
        val type: String = "NORMAL",
        val details: String = "",
        val complete: Boolean = false,
        val value: Int = 0,
        val award: String = "",
        val awardImage: String = ""
    ) : Parcelable
}
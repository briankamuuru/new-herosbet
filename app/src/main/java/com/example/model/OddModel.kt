package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class FreeOddModel(
    val id: String = "",
    val homeTeam: String = "",
    val awayTeam: String = "",
    val leagueName: String = "",
    val tip: String = "",
    val matchTime: String = "",
    val scores: String = "",
    val odds: String = "1.00",
    val isWon: Boolean = false,
    val isLost: Boolean = false
)

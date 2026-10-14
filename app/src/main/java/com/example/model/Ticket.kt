package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val id: String = "",
    val odds: List<FreeOddModel> = emptyList(),
    val ticketDate: String = "",
    val postedDate: Long = 0L,
    val type: String = ""
) {
    val totalOdds: Double
        get() {
            if (odds.isEmpty()) return 0.0
            return odds.fold(1.0) { acc, odd ->
                val v = odd.odds.toDoubleOrNull() ?: 1.0
                acc * v
            }
        }

    val totalOddsFormatted: String
        get() = String.format("%.2f", totalOdds)
}

package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Ticket

@Composable
fun TicketItemView(
    ticket: Ticket,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .padding(top = 10.dp, bottom = 15.dp)
            .testTag("ticket_item_${ticket.id}")
    ) {
        // Date & Total Odds banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFF215F23)) // Color.fromARGB(255, 33, 95, 35)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DATE: ${ticket.ticketDate}",
                color = Color.White,
                fontSize = 16.sp
            )

            if (ticket.odds.isNotEmpty()) {
                Text(
                    text = "Total Odds: ${ticket.totalOddsFormatted}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Odds list for this ticket
        ticket.odds.forEach { odd ->
            OddItemView(odd = odd)
        }
    }
}

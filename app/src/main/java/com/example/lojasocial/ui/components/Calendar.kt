package com.example.lojasocial.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.boguszpawlowski.composecalendar.Calendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import java.time.LocalDate


@Composable
fun InteractiveCalendar(
    availableDates: MutableSet<LocalDate>, // Conjunto de datas disponíveis
    onDateToggle: (LocalDate) -> Unit // Callback para alternar disponibilidade
) {
    val calendarState = rememberSelectableCalendarState()

    Calendar(
        calendarState = calendarState,
        dayContent = { day ->
            val isAvailable = availableDates.contains(day.date)
            Text(
                text = day.date.dayOfMonth.toString(),
                modifier = Modifier
                    .padding(4.dp)
                    .background(if (isAvailable) Color(0xFFFFA500) else Color.Transparent) // Laranja se disponível
                    .clickable { onDateToggle(day.date) } // Alterna disponibilidade
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isAvailable) Color.White else Color.Black,
            )
        }
    )
}


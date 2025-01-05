package com.example.lojasocial.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.ui.components.InteractiveCalendar
import org.threeten.bp.LocalDate

/*
@Composable
fun CalendarView(viewModel: CalendarViewModel = viewModel()) {
    val availableDates by viewModel.availableDates.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calendário",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        InteractiveCalendar(
            availableDates = availableDates.toMutableSet(),
            onDateToggle = { selectedDate ->
                viewModel.toggleAvailability(selectedDate)
            }
        )
    }
}
*/







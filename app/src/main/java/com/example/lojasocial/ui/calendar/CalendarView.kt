package com.example.lojasocial.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarViewVoluntario(
    onDaySelected: (LocalDate) -> Unit, // Callback para ação quando um dia é selecionado
    selectedDates: List<LocalDate>,    // Lista de datas selecionadas
) {
    val viewModel: CalendarViewModel = viewModel()
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    // Variaveis para controlar o calendário
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(6) }
    val endMonth = remember { currentMonth.plusMonths(6) }
    val daysOfWeek = daysOfWeek()
    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth
    )
    Box(
        modifier = Modifier
            .fillMaxSize() // Ensures the box covers the entire available area
            .background(color = Color.White) // Sets the background color to white
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(48.dp)
                    .background(
                        color = Color(0xFFBD4143),
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Text(
                    text = "Doações",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalCalendar(
                state = calendarState,
                dayContent = { day ->
                    val isSelected = day.date in selectedDates
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color.Green else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.date.dayOfMonth.toString(),
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                },
                monthHeader = { month ->
                    Text(
                        text = "${
                            month.yearMonth.month.name.lowercase()
                                .replaceFirstChar { it.uppercase() }
                        } ${month.yearMonth.year}",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        /* Ação para marcar disponibilidade */

                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143)),
                )
                {
                    Text("Marcar")
                }

                Button(
                    onClick = {
                        /* Ação para desmarcar disponibilidade */
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143)),
                )
                {
                    Text("Desmarcar")
                }
            }
        }
    }
}

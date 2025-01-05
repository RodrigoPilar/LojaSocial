package com.example.lojasocial.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarView(
    onDaySelected: (LocalDate) -> Unit, // Callback para ação quando um dia é selecionado
    selectedDates: List<LocalDate>,    // Lista de datas selecionadas
) {
    val viewModel: CalendarViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // Variáveis para controlar o calendário
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(6) }
    val endMonth = remember { currentMonth.plusMonths(6) }
    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth
    )

    Box(
        modifier = Modifier
            .fillMaxSize() // Garante que a caixa cubra toda a área disponível
            .background(color = Color.White) // Define a cor de fundo para branca
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de doações
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

            // Componente de calendário horizontal
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

            // Linhas de botões para marcar e desmarcar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        // Lógica para marcar disponibilidade
                        selectedDates.forEach {
                            // Para cada data selecionada, marque como disponível
                            viewModel.toggleAvailability(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143)),
                ) {
                    Text("Marcar")
                }

                Button(
                    onClick = {
                        // Lógica para desmarcar disponibilidade
                        selectedDates.forEach {
                            // Para cada data selecionada, remova da lista de disponível
                            viewModel.toggleAvailability(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143)),
                ) {
                    Text("Desmarcar")
                }
            }
        }
    }
}

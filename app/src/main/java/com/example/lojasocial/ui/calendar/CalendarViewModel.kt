package com.example.lojasocial.ui.calendar

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.threeten.bp.LocalDate

data class Calendario(
    var startMonth: String = "",
    var endMonth: String = "",
    var firstVisibleMonth: String = "",
    var firstDayOfWeek: String = "",
)

class CalendarViewModel : ViewModel() {

    // Fluxo de estado para gerenciar o calendário
    private val _state = MutableStateFlow(Calendario())
    val state: StateFlow<Calendario> = _state.asStateFlow()

    // Firestore para carregar e atualizar datas disponíveis
    private val firestore = FirebaseFirestore.getInstance()

    // Estado para armazenar as datas disponíveis
    private val _availableDates = mutableStateOf(setOf<LocalDate>())
    val availableDates: StateFlow<Set<LocalDate>> get() = _availableDates.asStateFlow()

    init {
        loadAvailableDates()
    }

    // Função para carregar datas disponíveis do Firestore
    private fun loadAvailableDates() {
        firestore.collection("availableDates")
            .get()
            .addOnSuccessListener { result ->
                val dates = result.documents.mapNotNull { doc ->
                    doc.getString("date")?.let { LocalDate.parse(it) }
                }.toSet()
                _availableDates.value = dates
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao carregar datas: ", e)
            }
    }

    // Alterna a disponibilidade de uma data e atualiza o Firestore
    fun toggleAvailability(date: LocalDate) {
        val updatedDates = _availableDates.value.toMutableSet()

        if (updatedDates.contains(date)) {
            // Remove a data se já estiver presente
            updatedDates.remove(date)
            firestore.collection("availableDates").document(date.toString()).delete()
        } else {
            // Adiciona a data se não estiver presente
            updatedDates.add(date)
            firestore.collection("availableDates").document(date.toString())
                .set(mapOf("date" to date.toString()))
        }

        // Atualiza o estado local com as novas datas disponíveis
        _availableDates.value = updatedDates
    }
}

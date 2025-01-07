package com.example.lojasocial.ui.calendar

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.lojasocial.ui.donations.DonationsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.firestore.FirebaseFirestore
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException


data class Calendario(
    var startMonth: String = "",
    var endMonth: String = "",
    var firstVisibleMonth: String = "",
    var firstDayOfWeek: String = "",
)

class CalendarViewModel : ViewModel() {
    private val _state = MutableStateFlow(Calendario())
    val state: StateFlow<Calendario> = _state

    private val firestore = FirebaseFirestore.getInstance()

    private val _availableDates = mutableStateOf(setOf<LocalDate>())
    //val availableDates: Thread.State<Set<LocalDate>> = _availableDates

    init {
        loadAvailableDates()
    }

    // Carrega as datas disponíveis do Firestore
    private fun loadAvailableDates() {
        firestore.collection("availableDates")
            .get()
            .addOnSuccessListener { result ->
                val dates = result.documents.mapNotNull { doc ->
                    LocalDate.parse(doc.getString("date"))
                }.toSet()
                _availableDates.value = dates
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao carregar datas: ", e)
            }
    }

    // Alterna a disponibilidade da data e atualiza no Firestore
    fun toggleAvailability(date: LocalDate) {
        val updatedDates = _availableDates.value.toMutableSet()
        if (updatedDates.contains(date)) {
            updatedDates.remove(date)
            firestore.collection("availableDates").document(date.toString()).delete()
        } else {
            updatedDates.add(date)
            firestore.collection("availableDates").document(date.toString())
                .set(mapOf("date" to date.toString()))
        }
        _availableDates.value = updatedDates
    }
}


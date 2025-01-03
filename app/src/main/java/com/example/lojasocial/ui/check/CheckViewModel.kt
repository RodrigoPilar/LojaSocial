package com.example.lojasocial.ui.check

import androidx.lifecycle.ViewModel
import com.example.lojasocial.repositories.CheckInOutRepository

// ViewModel responsável por gerir a lógica de Check-in e Check-out
class CheckViewModel : ViewModel() {

    // Instância do repositório que irá comunicar com o Firestore
    private val repository = CheckInOutRepository()

    // Função que será chamada pela UI para realizar o Check-in ou Check-out
    fun checkInOut(status: String, onResult: (Boolean) -> Unit) {
        // Chama o repositório para realizar o Check-in ou Check-out
        repository.checkInOut(status) { success ->
            // Retorna o resultado (sucesso ou falha) para a UI
            onResult(success)
        }
    }
}
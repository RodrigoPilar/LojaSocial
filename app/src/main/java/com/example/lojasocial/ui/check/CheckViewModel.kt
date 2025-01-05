package com.example.lojasocial.ui.check

import androidx.lifecycle.ViewModel

class CheckViewModel : ViewModel() {

    // Função que simula a lógica de Check-in
    fun performCheckIn(fullName: String, phoneNumber: String, familyStatus: String, isFirstVisit: Boolean) {
        // Aqui seria o código para fazer a chamada para o banco de dados (Firestore, API, etc.)
        // Para o exemplo, estamos apenas imprimindo os dados
        println("Check-in realizado:")
        println("Nome: $fullName")
        println("Telefone: $phoneNumber")
        println("Agregado Familiar: $familyStatus")
        println("Primeira visita: $isFirstVisit")
    }
}

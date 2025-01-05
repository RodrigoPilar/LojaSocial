package com.example.lojasocial.ui.check

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.example.lojasocial.models.Beneficiario

class CheckInViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    var nome by mutableStateOf("")
    var telefone by mutableStateOf("")
    var agregadoFamiliar by mutableStateOf("")
    var nacionalidade by mutableStateOf("")
    var freguesia by mutableStateOf("")
    var numVisitasComArtigos by mutableStateOf("")
    var numVisitasSemArtigos by mutableStateOf("")
    var advertenciasRecebidas by mutableStateOf("")
    var listaArtigos by mutableStateOf("")

    // Atualizadores
    fun onNomeChange(newNome: String) { nome = newNome }
    fun onTelefoneChange(newTelefone: String) { telefone = newTelefone }
    fun onAgregadoFamiliarChange(newAgregadoFamiliar: String) { agregadoFamiliar = newAgregadoFamiliar }
    fun onNacionalidadeChange(newNacionalidade: String) { nacionalidade = newNacionalidade }
    fun onFreguesiaChange(newFreguesia: String) { freguesia = newFreguesia }
    fun onNumVisitasComArtigosChange(newNum: String) { numVisitasComArtigos = newNum }
    fun onNumVisitasSemArtigosChange(newNum: String) { numVisitasSemArtigos = newNum }
    fun onAdvertenciasRecebidasChange(newValue: String) { advertenciasRecebidas = newValue }
    fun onListaArtigosChange(newLista: String) { listaArtigos = newLista }

    // Função para guardar a primeira visita
    fun guardarPrimeiraVisita(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val beneficiario = hashMapOf(
            "nome" to nome,
            "telefone" to telefone,
            "agregadoFamiliar" to agregadoFamiliar,
            "nacionalidade" to nacionalidade,
            "freguesia" to freguesia,
            "primeiraVisita" to true
        )

        db.collection("Beneficiário")
            .add(beneficiario)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Erro ao guardar os dados.")
            }
    }
}



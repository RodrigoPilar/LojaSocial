package com.example.lojasocial.ui.check

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.lojasocial.models.Beneficiario
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
    var mensagem by mutableStateOf<String?>(null) // Mensagem de feedback

    fun onNomeChange(newNome: String) { nome = newNome }
    fun onTelefoneChange(newTelefone: String) { telefone = newTelefone }
    fun onAgregadoFamiliarChange(newAgregadoFamiliar: String) { agregadoFamiliar = newAgregadoFamiliar }
    fun onNacionalidadeChange(newNacionalidade: String) { nacionalidade = newNacionalidade }
    fun onFreguesiaChange(newFreguesia: String) { freguesia = newFreguesia }
    fun onNumVisitasComArtigosChange(newNum: String) { numVisitasComArtigos = newNum }
    fun onNumVisitasSemArtigosChange(newNum: String) { numVisitasSemArtigos = newNum }
    fun onAdvertenciasRecebidasChange(newValue: String) { advertenciasRecebidas = newValue }
    fun onListaArtigosChange(newLista: String) { listaArtigos = newLista }

    // Validação dos campos obrigatórios
    private fun validarCamposObrigatorios(isFirstVisit: Boolean): Boolean {
        return if (isFirstVisit) {
            nome.isNotBlank() && telefone.isNotBlank() && agregadoFamiliar.isNotBlank() &&
                    nacionalidade.isNotBlank() && freguesia.isNotBlank()
        } else {
            nome.isNotBlank() && telefone.isNotBlank() && agregadoFamiliar.isNotBlank() &&
                    numVisitasComArtigos.isNotBlank() && numVisitasSemArtigos.isNotBlank() &&
                    advertenciasRecebidas.isNotBlank() && listaArtigos.isNotBlank()
        }
    }

    // Função para guardar a primeira visita
    fun guardarPrimeiraVisita(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (!validarCamposObrigatorios(isFirstVisit = true)) {
            mensagem = "Campos obrigatórios por preencher"
            return
        }

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
            .addOnSuccessListener {
                mensagem = "Registo concluído com sucesso!"
                onSuccess()
            }
            .addOnFailureListener { exception ->
                mensagem = exception.message ?: "Erro ao efetuar registo!"
                onFailure(mensagem!!)
            }
    }

    fun checkInPorNome(nome: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("Beneficiário")
            .whereEqualTo("nome", nome)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    val beneficiarioId = document.id
                    val telefone = document.getString("telefone") ?: ""
                    val agregadoFamiliar = document.getString("agregadoFamiliar") ?: ""

                    // Chama a função checkIn com os dados encontrados
                    checkIn(
                        beneficiarioId = beneficiarioId,
                        nome = nome,
                        telefone = telefone,
                        agregadoFamiliar = agregadoFamiliar,
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )
                } else {
                    onFailure("Beneficiário não encontrado.")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Erro ao buscar beneficiário.")
            }
    }

    fun checkIn(
        beneficiarioId: String,
        nome: String,
        telefone: String,
        agregadoFamiliar: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val beneficiarioNaLoja = hashMapOf(
            "beneficiarioId" to beneficiarioId,
            "nome" to nome,
            "telefone" to telefone,
            "agregadoFamiliar" to agregadoFamiliar
        )

        db.collection("BeneficiariosNaLoja")
            .add(beneficiarioNaLoja)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Erro ao efetuar check-in.")
            }
    }

    fun buscarBeneficiarioPorNome(
        nome: String,
        onSuccess: (Beneficiario) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("Beneficiário")
            .whereEqualTo("nome", nome)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val document = result.documents[0]
                    val beneficiario = document.toObject(Beneficiario::class.java)?.apply {
                        id = document.id
                    }
                    if (beneficiario != null) {
                        buscarVisitas(
                            beneficiarioId = beneficiario.id,
                            onSuccess = { onSuccess(beneficiario) }, // Passar beneficiário ao onSuccess
                            onFailure = onFailure
                        )
                    } else {
                        onFailure("Erro ao processar dados do beneficiário.")
                    }
                } else {
                    onFailure("Beneficiário não encontrado.")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Erro ao buscar beneficiário.")
            }
    }

    private fun contabilizarVisitas(visitas: List<Map<String, Any>>) {
        var visitasComArtigos = 0
        var visitasSemArtigos = 0
        val artigosControlados = mutableListOf<String>()

        visitas.forEach { visita ->
            val levaArtigos = visita["levaArtigos"] as? Boolean ?: false
            val artigosControladosFlag = visita["artigosControlados"] as? Boolean ?: false
            val descricaoArtigo = visita["descricaoArtigo"] as? String ?: ""

            if (levaArtigos) {
                visitasComArtigos++
            } else {
                visitasSemArtigos++
            }

            if (artigosControladosFlag) {
                artigosControlados.add(descricaoArtigo)
            }
        }

        numVisitasComArtigos = visitasComArtigos.toString()
        numVisitasSemArtigos = visitasSemArtigos.toString()
        listaArtigos = artigosControlados.joinToString(", ")
    }


    fun buscarVisitas(beneficiarioId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("Visitas")
            .whereEqualTo("beneficiarioId", beneficiarioId)
            .get()
            .addOnSuccessListener { result ->
                val visitas = result.documents.mapNotNull { it.data }
                contabilizarVisitas(visitas)
                onSuccess() // Chama o callback de sucesso
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Erro ao buscar visitas.")
            }
    }

    fun limparCampos() {
        telefone = ""
        agregadoFamiliar = ""
        nacionalidade = ""
        freguesia = ""
        numVisitasComArtigos = ""
        numVisitasSemArtigos = ""
        advertenciasRecebidas = ""
        listaArtigos = ""
    }

}





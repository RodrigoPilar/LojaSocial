package com.example.lojasocial.ui.donations

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import android.util.Log


data class DonationsState(
    val nome: String = "",
    val telefone: String = "",
    val data: String = "",
    val ficheiro: String = "",
    val outros: String = "",
    val valor: String = "",
    var tipo: String = "",
    val filePath: String = "",
    val errorMessage: String? = null // Campo para armazenar mensagens de erro
)

class DonationsViewModel : ViewModel() {
    private val _state = MutableStateFlow(DonationsState())
    val state: StateFlow<DonationsState> = _state
    private val _donations = MutableStateFlow<List<DonationsState>>(emptyList())
    val donations: StateFlow<List<DonationsState>> = _donations

    // Atualizar o campo Nome
    fun onNomeChange(nome: String) {
        _state.update { currentState -> currentState.copy(nome = nome) }
    }

    // Atualizar o campo Telefone
    fun onTelefoneChange(telefone: String) {
        _state.update { currentState -> currentState.copy(telefone = telefone) }
    }

    // Atualizar o campo Data
    fun onDataChange(data: String) {
        _state.update { currentState -> currentState.copy(data = data) }
    }

    // Atualizar o campo Data
    fun onFicheiroChange(ficheiro: String) {
        _state.update { currentState -> currentState.copy(ficheiro = ficheiro) }
    }

    // Atualizar o campo Outros
    fun onOutrosChange(outros: String) {
        _state.update { currentState -> currentState.copy(outros = outros) }
    }

    // Atualizar o campo Valor
    fun onValorChange(valor: String) {
        _state.update { currentState -> currentState.copy(valor = valor) }
    }

    fun onTipoChange(tipo: String) {
        _state.update { currentState -> currentState.copy(tipo = tipo) }
    }

    // Função para resetar o estado após guardar a doação
    fun onDonationSaved() {
        _state.update {
            DonationsState() // Reseta para os valores padrão
        }
    }

    // Função para guardar no Firestore
    fun guardar(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val donation = hashMapOf(
            "nome" to _state.value.nome,
            "telefone" to _state.value.telefone,
            "data" to _state.value.data,
            "ficheiro" to _state.value.ficheiro,
            "outros" to _state.value.outros,
            "valor" to _state.value.valor,
            "tipo" to _state.value.tipo,
            "filePath" to _state.value.filePath
        )

        if (_state.value.tipo.isBlank() || _state.value.data.isBlank()) {
            _state.update { it.copy(errorMessage = "Por favor, preencha todos os campos obrigatórios.") }
            onFailure("Por favor, preencha todos os campos obrigatórios.")
            return
        }

        FirebaseFirestore.getInstance()
            .collection("donations") // Nome da coleção no Firestore
            .add(donation)
            .addOnSuccessListener {
                onSuccess() // Chama o callback de sucesso
                onDonationSaved() // Limpa o formulário após sucesso
            }
            .addOnFailureListener { exception ->
                val errorMessage = exception.localizedMessage ?: "Erro ao guardar a doação."
                _state.update { it.copy(errorMessage = errorMessage) }
                onFailure(errorMessage) // Chama o callback de erro
            }
    }

    init {
        fetchDonations()
    }

    private fun fetchDonations() {
        FirebaseFirestore.getInstance()
            .collection("donations")
            .get()
            .addOnSuccessListener { result ->
                val donationsList = result.map { document ->
                    document.toObject(DonationsState::class.java)
                }
                _donations.value = donationsList
            }
            .addOnFailureListener { exception ->
                Log.e("DonationsViewModel", "Erro ao buscar doações", exception)
            }
    }

}


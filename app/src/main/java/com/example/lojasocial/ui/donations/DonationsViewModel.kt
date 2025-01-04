package com.example.lojasocial.ui.donations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class DonationsState(
    val nome: String = "",
    val telefone: String = "",
    val data: String = "",
    val ficheiro: String = "",
    val outros: String = "",
    val valor: String = "",
    var tipo: String = "",
    val filePath: String = "",
)

class DonationsViewModel : ViewModel() {
    private val _state = MutableStateFlow(DonationsState())
    val state: StateFlow<DonationsState> = _state

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

    //guardar doação
    fun guardar(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val nome = _state.value.nome
        val telefone = _state.value.telefone
        val data = _state.value.data
        val ficheiro = _state.value.ficheiro
        val outros = _state.value.outros
        val valor = _state.value.valor
        val tipo = _state.value.tipo
        val filePath = _state.value.filePath
        var errorMessage = ""

        if (tipo.isBlank() || data.isBlank()) {
            errorMessage = "Por favor, preencha todos os campos"
            _state.update { currentState ->
                currentState.copy(error(message = errorMessage))
            }
        } else if ((tipo == "Outros" && outros.isBlank()) ||
            (tipo == "Dinheiro" && valor.isBlank())
        ) {
            _state.update { currentState ->
                currentState.copy(error(message = errorMessage))
            }
            return
        } else {
            val donation = hashMapOf(
                "nome" to nome,
                "telefone" to telefone,
                "data" to data,
                "ficheiro" to ficheiro,
                "outros" to outros,
                "valor" to valor,
                "tipo" to tipo,
                "filePath" to filePath
            )
        } //Remover após logica

        /*
        // Criar Doação no Firebase
        Firebase
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Obter o UID do utilizador
                    val userId = task.result?.user?.uid ?: ""

                    // Guardar os dados adicionais no Firestore
                    val user = hashMapOf(
                        "nome" to nome,
                        "email" to email,
                        "telefone" to telefone,
                        "role" to "user"
                    )

                    FirebaseFirestore.getInstance()
                        .collection("utilizadores")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            _state.update { currentState ->
                                currentState.copy(isRegistered = true, errorMessage = null)
                            }
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            val errorMessage =
                                exception.localizedMessage ?: "Erro ao guardar os dados."
                            _state.update { currentState ->
                                currentState.copy(errorMessage = errorMessage)
                            }
                            onFailure(errorMessage)
                        }

                }

            }*/
    }

    //ao guardar doação
    fun onDonationSaved() {
        _state.update { currentState ->
            currentState.copy(
                nome = "",
                telefone = "",
                data = "",
                ficheiro = "",
                outros = "",
                valor = "",
                tipo = "",
                filePath = ""
            )
        }
    return
    }
}

package com.example.lojasocial.ui.registo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Classe de estado para o registo
data class RegistoState(
    val nome: String = "",
    val email: String = "",
    val password: String = "",
    val telefone: String = "",
    val errorMessage: String? = null,
    val isRegistered: Boolean = false // Indica se o registo foi bem-sucedido
)

class RegistoViewModel : ViewModel() {
    // Estado do registo gerido por StateFlow
    private val _state = MutableStateFlow(RegistoState())
    val state: StateFlow<RegistoState> = _state

    // Atualizar o campo Nome
    fun onNomeChange(nome: String) {
        _state.update { currentState ->
            currentState.copy(nome = nome)
        }
    }

    // Atualizar o campo Email
    fun onEmailChange(email: String) {
        _state.update { currentState ->
            currentState.copy(email = email)
        }
    }

    // Atualizar o campo Password
    fun onPasswordChange(password: String) {
        _state.update { currentState ->
            currentState.copy(password = password)
        }
    }

    // Atualizar o campo Telefone
    fun onTelefoneChange(telefone: String) {
        _state.update { currentState ->
            currentState.copy(telefone = telefone)
        }
    }

    // Realizar o registo
    fun registar(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val nome = _state.value.nome
        val email = _state.value.email
        val password = _state.value.password
        val telefone = _state.value.telefone

        if (nome.isBlank() || email.isBlank() || password.isBlank() || telefone.isBlank()) {
            _state.update { currentState ->
                currentState.copy(errorMessage = "Por favor, preencha todos os campos.")
            }
            return
        }

        // Criar utilizador no Firebase Authentication
        FirebaseAuth.getInstance()
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
                            val errorMessage = exception.localizedMessage ?: "Erro ao guardar os dados."
                            _state.update { currentState ->
                                currentState.copy(errorMessage = errorMessage)
                            }
                            onFailure(errorMessage)
                        }
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Erro ao criar a conta."
                    _state.update { currentState ->
                        currentState.copy(errorMessage = errorMessage)
                    }
                    onFailure(errorMessage)
                }
            }
    }
}

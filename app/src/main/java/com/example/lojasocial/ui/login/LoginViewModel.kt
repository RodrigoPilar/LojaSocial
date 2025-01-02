package com.example.lojasocial.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.google.firebase.auth.FirebaseAuth


data class LoginState(
    val email: String = "",
    val password: String = "",
    val role: String = "", // Pode ser "admin", "voluntário", etc.
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false // Booleano para indicar se o login foi bem-sucedido
)


class LoginViewModel : ViewModel() {
    // Estado do login gerido por StateFlow
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    // Atualizar o email no estado
    fun onEmailChange(email: String) {
        _state.update { currentState ->
            currentState.copy(email = email)
        }
    }

    // Atualizar a password no estado
    fun onPasswordChange(password: String) {
        _state.update { currentState ->
            currentState.copy(password = password)
        }
    }

    // Realizar o login
    fun login(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val email = _state.value.email
        val password = _state.value.password

        if (email.isBlank() || password.isBlank()) {
            _state.update { currentState ->
                currentState.copy(errorMessage = "Por favor, preencha todos os campos.")
            }
            return
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.update { currentState ->
                        currentState.copy(isLoggedIn = true, errorMessage = null)
                    }
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Erro ao iniciar sessão."
                    _state.update { currentState ->
                        currentState.copy(errorMessage = errorMessage)
                    }
                    onFailure(errorMessage)
                }
            }
    }
}



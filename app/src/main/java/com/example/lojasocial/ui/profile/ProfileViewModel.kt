package com.example.lojasocial.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await


data class ProfileState(
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val newPassword: String = "",
    val currentPassword: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class ProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    fun onNewPasswordChange(newPassword: String) {
        _state.update { it.copy(newPassword = newPassword) }
    }

    fun onCurrentPasswordChange(currentPassword: String) {
        _state.update { it.copy(currentPassword = currentPassword) }
    }

    fun saveProfileChanges(newPassword: String, confirmPassword: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            _state.update { it.copy(errorMessage = "Utilizador não autenticado.") }
            return
        }

        // Verifica se as passwords coincidem
        if (newPassword != confirmPassword) {
            _state.update { it.copy(errorMessage = "As passwords não coincidem.") }
            return
        }

        // Atualiza a password no Firebase Authentication
        currentUser.updatePassword(newPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _state.update { it.copy(successMessage = "Password atualizada com sucesso!") }
            } else {
                val errorMessage = task.exception?.localizedMessage ?: "Erro ao atualizar password."
                _state.update { it.copy(errorMessage = errorMessage) }
            }
        }
    }



    suspend fun loadProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            try {
                val document = FirebaseFirestore.getInstance()
                    .collection("utilizadores")
                    .document(currentUser.uid)
                    .get()
                    .await()

                val nome = document.getString("nome") ?: ""
                val email = document.getString("email") ?: ""
                val telefone = document.getString("telefone") ?: ""

                _state.update {
                    it.copy(nome = nome, email = email, telefone = telefone, errorMessage = null)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(errorMessage = "Erro ao carregar perfil: ${e.localizedMessage}")
                }
            }
        }
    }
}








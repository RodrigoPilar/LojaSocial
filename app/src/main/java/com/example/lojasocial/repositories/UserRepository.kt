package com.example.lojasocial.repositories

import com.example.lojasocial.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "" // e.g., "admin", "voluntário", etc.
)

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getUserRole(): String {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: return "user" // Se não houver utilizador autenticado, assume "user"

        return try {
            val document = firestore.collection("utilizadores")
                .document(uid)
                .get()
                .await()

            // Obtemos a role do documento, ou "user" se não for encontrada
            document.getString("role") ?: "user"
        } catch (e: Exception) {
            "user" // Em caso de erro, assume "user"
        }
    }

}




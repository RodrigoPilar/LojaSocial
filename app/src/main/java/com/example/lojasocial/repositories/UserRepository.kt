package com.example.lojasocial.repositories

import com.example.lojasocial.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "" // e.g., "admin", "voluntário", etc.
)

class UserRepository(private val firestore: FirebaseFirestore) {

    private val usersCollection = firestore.collection("users")

    // Obter dados do utilizador pelo ID
    fun getUserById(
        userId: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        usersCollection.document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject<User>()
                    if (user != null) {
                        onSuccess(user)
                    } else {
                        onFailure(Exception("Erro ao converter os dados do utilizador."))
                    }
                } else {
                    onFailure(Exception("Utilizador não encontrado no Firestore."))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
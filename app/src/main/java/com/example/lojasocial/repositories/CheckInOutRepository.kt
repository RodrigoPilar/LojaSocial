package com.example.lojasocial.repositories

import com.google.firebase.firestore.FirebaseFirestore

class CheckInOutRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun checkInUser(
        fullName: String,
        phoneNumber: String,
        familyStatus: String,
        isFirstVisit: Boolean
    ) {
        val userData = hashMapOf(
            "fullName" to fullName,
            "phoneNumber" to phoneNumber,
            "familyStatus" to familyStatus,
            "isFirstVisit" to isFirstVisit
        )

        firestore.collection("users")
            .add(userData)
            .addOnSuccessListener {
                // Caso o salvamento no Firestore seja bem-sucedido
            }
            .addOnFailureListener {
                // Caso ocorra um erro ao salvar os dados
            }
    }
}

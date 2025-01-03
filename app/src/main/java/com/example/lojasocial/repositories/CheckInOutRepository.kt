package com.example.lojasocial.repositories

import com.example.lojasocial.ui.check.CheckInOutState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CheckInOutRepository {

    // Instância do Firestore para interagir com a base de dados
    private val firestore = FirebaseFirestore.getInstance()

    // Função para realizar o Check-in (atualiza o estado no Firestore)
    suspend fun checkIn(userId: String): CheckInOutState {
        return try {
            // Aceder à coleção 'users' e atualizar o campo 'checkInStatus' para 'checked-in'
            firestore.collection("users")
                .document(userId) // Substituir "user1" pelo ID do utilizador
                .update("checkInStatus", "checked-in")
                .await() // Aguarda a conclusão da operação do Firestore
            // Se a atualização for bem-sucedida, retorna o estado de check-in
            CheckInOutState("checked-in")
        } catch (e: Exception) {
            // Se ocorrer um erro, retorna o estado de erro
            CheckInOutState("error")
        }
    }

    // Função para realizar o Check-out (atualiza o estado no Firestore)
    suspend fun checkOut(userId: String): CheckInOutState {
        return try {
            // Aceder à coleção 'users' e atualizar o campo 'checkInStatus' para 'checked-out'
            firestore.collection("users")
                .document(userId) // Substituir "user1" pelo ID do utilizador
                .update("checkInStatus", "checked-out")
                .await() // Aguarda a conclusão da operação do Firestore
            // Se a atualização for bem-sucedida, retorna o estado de check-out
            CheckInOutState("checked-out")
        } catch (e: Exception) {
            // Se ocorrer um erro, retorna o estado de erro
            CheckInOutState("error")
        }
    }
}
package com.example.lojasocial.ui.check

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.Screen
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.logging.Logger

class CheckOutViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _beneficiariosNaLoja = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val beneficiariosNaLoja: StateFlow<List<Map<String, Any>>> = _beneficiariosNaLoja

    var nome by mutableStateOf("")
    var telefone by mutableStateOf("")
    var agregadoFamiliar by mutableStateOf("")
    var beneficiarioId by mutableStateOf("")

    init {
        carregarBeneficiariosNaLoja()
    }

    private fun carregarBeneficiariosNaLoja() {
        val documentRef = db.collection("BeneficiariosNaLoja").document()

        documentRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val beneficiarioNaLoja = hashMapOf(
                    "beneficiarioId" to beneficiarioId,
                    "nome" to nome,
                    "telefone" to telefone,
                    "agregadoFamiliar" to agregadoFamiliar
                )

                documentRef.set(beneficiarioNaLoja)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Tabela criada com sucesso")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Erro a criar o documento", e)
                    }
            }
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error getting document", e)
            return@addOnFailureListener
        }

        db.collection("BeneficiariosNaLoja")
            .get()
            .addOnSuccessListener { result ->
                val beneficiarios = result.documents.map { document ->
                    document.data?.toMutableMap()?.apply {
                        this["id"] = document.id
                    } ?: emptyMap()
                }
                _beneficiariosNaLoja.value = beneficiarios
            }
            .addOnFailureListener { exception ->
                Logger.getLogger("CheckOutViewModel")
                    .warning("Erro ao carregar beneficiarios na loja: ${exception.message}")
            }
    }


    fun checkOut(
        beneficiario: Map<String, Any>,
        levaArtigos: Boolean,
        quantidade: Int?,
        artigosControlados: Boolean,
        descricaoArtigo: String?
    ) {
        val visita = hashMapOf(
            "beneficiarioId" to (beneficiario["id"] ?: ""),
            "nome" to (beneficiario["nome"] ?: ""),
            "levaArtigos" to levaArtigos,
            "quantidade" to (quantidade ?: 0),
            "artigosControlados" to artigosControlados,
            "descricaoArtigo" to (descricaoArtigo ?: ""),
        )

        val beneficiarioId = beneficiario["id"] as? String ?: return

        println("DEBUG: Chegou ao Checkout: $visita")

        val collectionRef = db.collection("BeneficiariosNaLoja")

        viewModelScope.launch {
            try {
                /*
                // 1) First, find the docId from BeneficiariosNaLoja
                val querySnapshot = db.collection("BeneficiariosNaLoja")
                    .whereEqualTo("beneficiarioId", beneficiarioId)
                    .get()
                    .await()

                // If no matching documents, do something (or just return)
                if (querySnapshot.isEmpty) {
                    println("DEBUG: No matching document for beneficiarioId = $beneficiarioId")
                    return@launch
                }

                val doc = querySnapshot.documents[0]
                val docId = doc.id
                println("DEBUG: Found docId = $docId")
*/
                db.collection("Visitas")
                    .add(visita)
                    .await()

                db.collection("BeneficiariosNaLoja")
                    .document(beneficiarioId).delete()
                    .await()

                println("DEBUG: ON SUCCESS Checkout BENID: $beneficiarioId")


                _beneficiariosNaLoja.value = _beneficiariosNaLoja.value.filter {
                    it["beneficiarioId"] != beneficiarioId
                }

            } catch (e: Exception) {
                println("DEBUG: ON Fail Checkout BENID: $beneficiarioId -> ${e.message}")
            }
        }
    }
}

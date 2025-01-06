package com.example.lojasocial.ui.check

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.util.logging.Logger

class CheckOutViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _beneficiariosNaLoja = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val beneficiariosNaLoja: StateFlow<List<Map<String, Any>>> = _beneficiariosNaLoja

    init {
        carregarBeneficiariosNaLoja()
    }

    private fun carregarBeneficiariosNaLoja() {
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
                Logger.getLogger("CheckOutViewModel").warning("Erro ao carregar beneficiarios na loja: ${exception.message}")
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
            "descricaoArtigo" to (descricaoArtigo ?: "" ),
        )

        val beneficiarioId = beneficiario["id"] as? String ?: return

        db.collection("Visitas")
            .add(visita)
            .addOnSuccessListener {
                db.collection("BeneficiariosNaLoja").document(beneficiarioId).delete()
                    .addOnSuccessListener {
                        _beneficiariosNaLoja.value = _beneficiariosNaLoja.value.filter {
                            it["id"] != beneficiarioId
                        }
                    }
                    .addOnFailureListener { exception ->
                        Logger.getLogger("CheckOutViewModel").warning("Erro ao remover beneficiário da coleção BeneficiariosNaLoja: ${exception.message}")
                    }
            }
            .addOnFailureListener { exception ->
                Logger.getLogger("CheckOutViewModel").warning("Erro ao registrar a visita: ${exception.message}")
            }
    }
}

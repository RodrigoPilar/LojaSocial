package com.example.lojasocial.models

data class Beneficiario(
    val nome: String = "",
    val telefone: String = "",
    val agregadoFamiliar: String = "",
    val nacionalidade: String = "",
    val freguesia: String = "",
    val primeiraVisita: Boolean = false
)
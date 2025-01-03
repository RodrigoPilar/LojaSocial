package com.example.lojasocial.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "" // Pode ser "admin", "voluntário", etc.
)
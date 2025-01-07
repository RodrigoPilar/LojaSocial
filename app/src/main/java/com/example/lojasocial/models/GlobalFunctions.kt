package com.example.lojasocial.models

fun sanitizeInput(input: String): String {
    return input.trim().replace(Regex("\\s+"), " ")
}

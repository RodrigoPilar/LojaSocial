package com.example.lojasocial.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onUserClick: () -> Unit // Callback para o botão do utilizador
) {
    TopAppBar(
        title = {}, // Sem título na TopBar
        navigationIcon = {
            IconButton(onClick = onUserClick) {
                Icon(
                    imageVector = Icons.Default.AccountCircle, // Ícone do utilizador
                    contentDescription = "Perfil do utilizador",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary, // Cor de fundo
            navigationIconContentColor = Color.White // Cor do ícone
        ),
        modifier = androidx.compose.ui.Modifier.height(56.dp)
    )
}
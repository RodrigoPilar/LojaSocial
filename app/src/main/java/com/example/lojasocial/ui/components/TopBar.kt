package com.example.lojasocial.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    TopAppBar(
        title = {}, // Sem título na TopBar
        navigationIcon = {
            UserMenu( // Aqui chamamos o UserMenu
                onProfileClick = onProfileClick,
                onLogoutClick = onLogoutClick
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFBD4143), // Cor de fundo da TopBar
            navigationIconContentColor = Color.White, // Cor do ícone
        ),
        modifier = Modifier.height(56.dp)
    )
}

package com.example.lojasocial.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle

@Composable
fun UserMenu(onProfileClick: () -> Unit, onLogoutClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Abrir menu do utilizador",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color(0xFFBD3232))
        ) {

            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onProfileClick()
                },
                text = {
                    Text(
                        text = "Perfil",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            )

            Divider(color = Color.White)

            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onLogoutClick()
                },
                text = {
                    Text(
                        text = "Terminar sessão",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            )
        }
    }
}
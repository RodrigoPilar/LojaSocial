package com.example.lojasocial.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.ui.components.Popup


@Composable
fun ProfileView(
    viewModel: ProfileViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    var showPopup by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFFB71C1C),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo de Nome Completo (bloqueado)
        OutlinedTextField(
            value = state.nome,
            onValueChange = {},
            label = { Text("Nome Completo") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Email (bloqueado)
        OutlinedTextField(
            value = state.email,
            onValueChange = {},
            label = { Text("Email") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Telefone (bloqueado)
        OutlinedTextField(
            value = state.telefone,
            onValueChange = {},
            label = { Text("Nº Telefone") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para mudar password
        Button(
            onClick = { showPopup = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB93535)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Alterar Password")
        }

        if (showPopup) {
            Popup(
                title = "Alterar Password",
                onConfirm = { newPassword, confirmPassword ->
                    viewModel.saveProfileChanges(newPassword, confirmPassword)
                    showPopup = false
                },
                onDismiss = { showPopup = false }
            )
        }


        // Mensagens de erro e sucesso
        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        state.successMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

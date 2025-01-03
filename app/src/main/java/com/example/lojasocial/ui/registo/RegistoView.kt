package com.example.lojasocial.ui.registo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun RegistoView(
    modifier: Modifier = Modifier,
    onRegistrationSuccess: () -> Unit = {}
) {
    // Inicializa o ViewModel e observa o estado
    val viewModel: RegistoViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de Nome Completo
        TextField(
            value = state.nome,
            onValueChange = { viewModel.onNomeChange(it) },
            label = { Text("Nome Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Email
        TextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Password
        TextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Telefone
        TextField(
            value = state.telefone,
            onValueChange = { viewModel.onTelefoneChange(it) },
            label = { Text("Nº Telemóvel") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de Registo
        Button(
            onClick = {
                viewModel.registar(
                    onSuccess = {
                        Toast.makeText(context, "Registo efetuado com sucesso!", Toast.LENGTH_SHORT).show()
                        onRegistrationSuccess()
                    },
                    onFailure = {
                        Toast.makeText(context, "Erro ao efetuar registo.", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registar")
        }

        // Exibe mensagem de erro se houver
        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}





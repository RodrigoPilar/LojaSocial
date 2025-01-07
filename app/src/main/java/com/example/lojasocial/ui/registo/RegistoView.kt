package com.example.lojasocial.ui.registo

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.R


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
            .background(Color(0xFFEFEFEF))
    ) {
        // Seção do logotipo (topo vermelho)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFBD4143))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Logotipo
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Logotipo",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Título "Apoio360"
                Text(
                    text = "Apoio360",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Conteúdo principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título "Registo"
            Text(
                text = "Registo",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A4F71)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Campo de Nome Completo
            TextField(
                value = state.nome,
                onValueChange = { viewModel.onNomeChange(it) },
                label = { Text("Nome Completo") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Email
            TextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Password
            TextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Nº Telemóvel
            TextField(
                value = state.telefone,
                onValueChange = { viewModel.onTelefoneChange(it) },
                label = { Text("Nº Telemóvel") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Registar", color = Color.White, fontSize = 16.sp)
            }

            // Exibe mensagem de erro se houver
            state.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}





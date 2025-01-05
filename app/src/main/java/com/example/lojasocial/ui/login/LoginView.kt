package com.example.lojasocial.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun LoginView(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegisto: () -> Unit = {}
) {
    //Inicializa o ViewModel e observa o estado
    val viewModel: LoginViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {
        // Logotipo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFBD4143)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo), // Substituir pelo ID do logotipo
                    contentDescription = "Logotipo",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                .weight(2f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título "Login"
            Text(
                text = "Login",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A4F71)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de email
            TextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp)),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de password
            TextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp)),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de login
            Button(
                onClick = {
                    viewModel.login(
                        onSuccess = {
                            Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        },
                        onFailure = {
                            Toast.makeText(context, "Credenciais inseridas inválidas!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Iniciar sessão", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Link para registo
            TextButton(onClick = { onNavigateToRegisto() }) {
                Text(
                    text = "Não tem conta? Registe-se já",
                    color = Color(0xFFBD4143),
                    fontSize = 14.sp
                )
            }
        }
    }
}




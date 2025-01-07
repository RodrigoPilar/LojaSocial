package com.example.lojasocial.ui.check

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.ui.components.CheckOutContainer
import kotlinx.coroutines.delay

@Composable
fun CheckOutView(viewModel: CheckOutViewModel = viewModel()) {
    val context = LocalContext.current
    val beneficiariosNaLoja by viewModel.beneficiariosNaLoja.collectAsState()
    var selectedBeneficiary by remember { mutableStateOf<Map<String, Any>?>(null) }
    var showContainer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título visual "Check Out"
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(48.dp)
                .background(
                    color = Color(0xFFBD4143), shape = RoundedCornerShape(50)
                )
        ) {
            Text(
                text = "Check Out",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(beneficiariosNaLoja) { beneficiario ->
                var showContainer by remember { mutableStateOf(false) }
                val nome = beneficiario["nome"] as? String ?: "Nome não disponível"
                val telefone = beneficiario["telefone"] as? String ?: "Sem telefone"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Nome: $nome", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "Telefone: $telefone", style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { showContainer = !showContainer },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143))
                        ) {
                            Text(text = "Check-Out", color = Color.White)
                        }

                        // Mostrar CheckOutContainer quando o botão for clicado
                        if (showContainer) {
                            Spacer(modifier = Modifier.height(8.dp))
                            CheckOutContainer(onCheckOut = { levaArtigos, quantidade, artigosControlados, descricaoArtigo ->
                                println("DEBUG: onCheckOut called with: levaArtigos = $levaArtigos, quantidade=$quantidade")
                                println("DEBUG: beneficiario data: $beneficiario")
                                viewModel.checkOut(
                                    beneficiario,
                                    levaArtigos,
                                    quantidade,
                                    artigosControlados,
                                    descricaoArtigo
                                )
                                showContainer = false // Fechar container após o check-out

                            }, onCancel = {
                                showContainer = false // Fechar container ao cancelar
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BeneficiarioItem(
    nome: String, telefone: String, onCheckOutClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Nome: $nome", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Telefone: $telefone", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onCheckOutClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143))
            ) {
                Text(text = "Check-Out", color = Color.White)
            }
        }
    }
}

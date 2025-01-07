package com.example.lojasocial.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.ui.check.CheckOutViewModel

@Composable
fun CheckOutContainer(
    onCheckOut: (Boolean, Int?, Boolean, String?) -> Unit,
    onCancel: () -> Unit
) {
    var levaArtigos by remember { mutableStateOf(false) }
    var artigosControlados by remember { mutableStateOf(false) }
    var quantidade by remember { mutableStateOf("") }
    var descricaoArtigoControlado by remember { mutableStateOf("") }
    var quantidadeError by remember { mutableStateOf(false) }
    val viewModel: CheckOutViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Switch "Leva artigos?"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Leva artigos?", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = levaArtigos,
                onCheckedChange = { levaArtigos = it }
            )
        }

        // Campo "Quantidade" - Só aparece se "Leva artigos?" estiver ativo
        if (levaArtigos) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = quantidade,
                onValueChange = {
                    quantidade = it
                    quantidadeError = it.toIntOrNull() == null
                },
                label = { Text("Quantidade") },
                modifier = Modifier.fillMaxWidth(),
                isError = quantidadeError
            )
            if (quantidadeError) {
                Text(
                    text = "Por favor, insira um número válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Switch "Artigos controlados?" - Desativado se "Leva artigos?" estiver off
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Artigos controlados?", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = artigosControlados,
                onCheckedChange = { artigosControlados = it },
                enabled = levaArtigos // Desativado quando "Leva artigos?" estiver off
            )
        }

        // Campo "Descrição do artigo controlado" - Só aparece se "Artigos controlados?" estiver ativo
        if (artigosControlados) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = descricaoArtigoControlado,
                onValueChange = { descricaoArtigoControlado = it },
                label = { Text("Descrição do artigo controlado") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botões para Confirmar ou Cancelar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancelar", color = Color.White)
            }

            Button(
                onClick = {
                    val quantidadeInt = quantidade.toIntOrNull()
                    if (levaArtigos && quantidadeInt == null) {
                        quantidadeError = true
                        return@Button
                    }
                    val descricao = if (artigosControlados) descricaoArtigoControlado else null

                    onCheckOut(levaArtigos, quantidadeInt ?: 0, artigosControlados, descricao)

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143))
            ) {
                Text("Confirmar Check-Out", color = Color.White)
            }
        }
    }
}

package com.example.lojasocial.ui.check

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.models.Beneficiario
import com.example.lojasocial.models.sanitizeInput
import com.example.lojasocial.ui.check.CheckInViewModel


@Composable
fun CheckInView(viewModel: CheckInViewModel = viewModel()) {
    var isFirstVisit by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(48.dp)
                .background(
                    color = Color(0xFFBD4143),
                    shape = RoundedCornerShape(50)
                )
        ) {
            Text(
                text = "Check-in",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Switch para "Primeira Visita?"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Primeira visita?", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = isFirstVisit,
                onCheckedChange = { isFirstVisit = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Nome Completo
        OutlinedTextField(
            value = viewModel.nome,
            onValueChange = {
                viewModel.onNomeChange(it)
                if (it.isNotBlank() && !isFirstVisit) {
                    viewModel.buscarBeneficiarioPorNome(
                        nome = sanitizeInput(it),
                        onSuccess = { beneficiario ->
                            viewModel.onTelefoneChange(beneficiario.telefone)
                            viewModel.onAgregadoFamiliarChange(beneficiario.agregadoFamiliar)
                        },
                        onFailure = { mensagem ->
                            Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    viewModel.limparCampos()
                }
            },
            label = { Text("Nome Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.telefone,
            onValueChange = { viewModel.onTelefoneChange(it) },
            label = { Text("Nº Telefone") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.agregadoFamiliar,
            onValueChange = { viewModel.onAgregadoFamiliarChange(it) },
            label = { Text("Agregado Familiar") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campos dinâmicos
        if (isFirstVisit) {
            OutlinedTextField(
                value = viewModel.nacionalidade,
                onValueChange = { viewModel.onNacionalidadeChange(it) },
                label = { Text("Nacionalidade") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.freguesia,
                onValueChange = { viewModel.onFreguesiaChange(it) },
                label = { Text("Freguesia/Zona de Residência") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedTextField(
                value = viewModel.numVisitasComArtigos,
                onValueChange = { },
                label = { Text("Nº de Visitas a levantar artigos") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.numVisitasSemArtigos,
                onValueChange = { },
                label = { Text("Nº de Visitas sem levantar artigos") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.listaArtigos,
                onValueChange = { },
                label = { Text("Lista de Artigos Controlados Levantados") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão dinâmico
        if (isFirstVisit) {
            Button(
                onClick = {
                    viewModel.guardarPrimeiraVisita(
                        onSuccess = {
                            Toast.makeText(context, "Registo concluído com sucesso!", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { mensagem ->
                            Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143))
            ) {
                Text("Registar Beneficiário", color = Color.White)
            }
        } else {
            Button(
                onClick = {
                    viewModel.checkInPorNome(
                        nome = viewModel.nome,
                        onSuccess = {
                            Toast.makeText(context, "Check-in realizado com sucesso!", Toast.LENGTH_SHORT).show()
                            viewModel.limparCampos()
                        },
                        onFailure = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143))
            ) {
                Text("Check-in", color = Color.White)
            }
        }
    }
}

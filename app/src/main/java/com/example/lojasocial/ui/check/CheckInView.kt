package com.example.lojasocial.ui.check

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojasocial.models.Beneficiario


@Composable
fun CheckInView(viewModel: CheckInViewModel = viewModel()) {
    var isFirstVisit by remember { mutableStateOf(false) }

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

        // Campos comuns (Nome, Telemóvel, Agregado Familiar)
        OutlinedTextField(
            value = viewModel.nome,
            onValueChange = { viewModel.onNomeChange(it) },
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

        // Campos dinâmicos baseados no estado do Switch
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
                onValueChange = { viewModel.onNumVisitasComArtigosChange(it) },
                label = { Text("Nº de Visitas com artigos") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.numVisitasSemArtigos,
                onValueChange = { viewModel.onNumVisitasSemArtigosChange(it) },
                label = { Text("Nº de Visitas sem artigos") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.advertenciasRecebidas,
                onValueChange = { viewModel.onAdvertenciasRecebidasChange(it) },
                label = { Text("Advertências Recebidas") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.listaArtigos,
                onValueChange = { viewModel.onListaArtigosChange(it) },
                label = { Text("Lista de Artigos Controlados Levantados") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão dinâmico (dois botões diferentes)
        if (isFirstVisit) {
            Button(
                onClick = {
                    viewModel.guardarPrimeiraVisita(
                        onSuccess = {  "Registo Concluido"  },
                        onFailure = { "Erro ao efetuar registo! " }
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
                    //viewModel.checkIn(
                       // onSuccess = { /* Sucesso */ },
                        //onFailure = { /* Falha */ }
                    //)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143))
            ) {
                Text("Check-in", color = Color.White)
            }
        }
    }
}


@Composable
fun InputField(label: String, showSearchIcon: Boolean = false, multiline: Boolean = false) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text(label) },
        trailingIcon = if (showSearchIcon) {
            { Icon(Icons.Default.Search, contentDescription = "Search") }
        } else null,
        modifier = Modifier.fillMaxWidth(),
        maxLines = if (multiline) 5 else 1
    )
}

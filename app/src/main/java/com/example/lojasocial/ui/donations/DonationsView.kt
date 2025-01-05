package com.example.lojasocial.ui.donations

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults


@Composable
fun donationsView(
    modifier: Modifier = Modifier,
    onFileSelected: (Uri) -> Unit = {},
    isAdmin: Boolean = false, // Parâmetro para verificar se é admin
    onConsultarDoacoesClick: () -> Unit // Callback para o botão "Consultar Doações"

) {
    // Inicializa o ViewModel e observa o estado
    val viewModel: DonationsViewModel = viewModel()
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    // Boolean para controlar o estado do dropdown
    var expanded by remember { mutableStateOf(false) }
    // Lista de opções para o dropdown
    var opcoes = listOf("Roupa", "Brinquedos", "Dinheiro", "Acessórios", "Outros")
    // Variável para armazenar o texto selecionado
    var selectedText by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    // Variável para armazenar o nome do ficheiro selecionado
    var selectedFileName by remember { mutableStateOf("") }
    // Variáveis para controlar a visibilidade dos campos
    var showOutros by remember { mutableStateOf(false) }
    var showValor by remember { mutableStateOf(false) }



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Additional padding for aesthetics
    ) {

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
                text = "Doações",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Nome Completo
        TextField(
            value = state.nome,
            onValueChange = { viewModel.onNomeChange(it) },
            label = { Text("Nome Completo (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Telefone
        TextField(
            value = state.telefone,
            onValueChange = { viewModel.onTelefoneChange(it) },
            label = { Text("Telefone (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown
        // Campo de Tipo de Doação
        TextField(
            value = state.tipo,
            onValueChange = { viewModel.onTipoChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text("Tipo de doação") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        //Dropdown tipo de doação
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current)
                { textFieldSize.width.toDp() })
        ) {
            opcoes.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        state.tipo = label
                        expanded = false
                        showOutros = state.tipo == "Outros" // Mostra o textField caso seja Outros
                        showValor =
                            state.tipo == "Dinheiro" // Mostra o textField caso seja Dinheiro
                        Log.d("DonationsView", "Selected Option: $state.tipo")
                    }
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Data de Doação
        TextField(
            value = state.data,
            onValueChange = { viewModel.onDataChange(it) },
            label = { Text("Data de Doação (DD/MM/AAAA)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // File picker launcher
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            if (uri != null) {
                selectedFileName = uri.lastPathSegment ?: "Ficheiro desconhecido"
                onFileSelected(uri)
            }
        }

        // Campo de Anexar ficheiro
        TextField(
            value = state.ficheiro,
            onValueChange = { viewModel.onFicheiroChange(it) },
            label = { Text("Anexar Ficheiro (Opcional)") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Carregar Ficheiro",
                    modifier = Modifier.clickable {
                        launcher.launch(arrayOf("*/*"))
                    }
                )
            }
        )

        if (selectedFileName.isNotEmpty()) {
            Text(
                text = "Ficheiro Selecionado: $selectedFileName",
                modifier = Modifier.padding(14.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (showOutros) {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = state.outros,
                onValueChange = { viewModel.onOutrosChange(it) },
                label = { Text("Especifique o tipo de doação") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (showValor) {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = state.valor,
                onValueChange = { viewModel.onValorChange(it) },
                label = { Text("Especifique a quantia €") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Botão de Guardar
        Button(
            onClick = {
                viewModel.guardar(
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Doação guardada com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.onDonationSaved()
                    },
                    onFailure = {
                        Toast.makeText(context, "Erro ao guardar a doação.", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBD4143)),
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão "Consultar Doações" visível apenas para admin
        if (isAdmin) {
            Button(
                onClick = { onConsultarDoacoesClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
            ) {
                Text("Consultar Doações")
            }

        }
    }
}
package com.example.lojasocial.ui.donations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lojasocial.ui.components.DonationItem

@Composable
fun DonationsListView() {
    val viewModel: DonationsViewModel = viewModel()
    val donations = viewModel.donations.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                text = "Doações Recebidas",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        // Lista de doações
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(donations) { donation ->
                DonationItem(donation = donation)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}






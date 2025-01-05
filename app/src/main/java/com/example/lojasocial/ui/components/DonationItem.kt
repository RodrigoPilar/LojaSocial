package com.example.lojasocial.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lojasocial.ui.donations.Donation
import com.example.lojasocial.ui.donations.DonationsState

@Composable
fun DonationItem(donation: DonationsState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Text("Nome: ${donation.nome}")
            Text("Tipo: ${donation.tipo}")
            if (donation.valor.isNotEmpty()) {
                Text("Valor: ${donation.valor} €")
            }
            Text("Data: ${donation.data}")
        }
    }
}

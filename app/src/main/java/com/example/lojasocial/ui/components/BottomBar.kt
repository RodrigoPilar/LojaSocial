package com.example.lojasocial.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lojasocial.R


@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onCheckInClick: () -> Unit,
    onCheckOutClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onDonationsClick: () -> Unit,
    onPortalClick: () -> Unit,
    showPortal: Boolean
) {
    BottomAppBar(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth(), // Garante que ocupa toda a largura
        containerColor = Color(0xFFBD4143)
    ) {
        // Distribuir ícones uniformemente com Row e weight
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Espaçamento uniforme
        ) {
            BottomNavigationIcon(
                icon = Icons.Default.Home,
                contentDescription = "Home",
                onClick = onHomeClick
            )
            BottomNavigationIcon(
                icon = R.drawable.ic_checkin,
                contentDescription = "Check In",
                onClick = onCheckInClick
            )
            BottomNavigationIcon(
                icon = R.drawable.ic_checkout,
                contentDescription = "Check Out",
                onClick = onCheckOutClick
            )
            BottomNavigationIcon(
                icon = R.drawable.ic_calendar,
                contentDescription = "Calendário",
                onClick = onCalendarClick
            )
            BottomNavigationIcon(
                icon = R.drawable.ic_donation,
                contentDescription = "Doações",
                onClick = onDonationsClick
            )
            if (showPortal) { // Mostra o botão Portal apenas se showPortal for true
                BottomNavigationIcon(
                    icon = R.drawable.ic_rateview,
                    contentDescription = "Portal de Avaliação",
                    onClick = onPortalClick
                )
            }
        }
    }
}

@Composable
fun BottomNavigationIcon(
    icon: Any,
    contentDescription: String,
    onClick: () -> Unit,
    isActive: Boolean = false
) {
    IconButton(onClick = onClick) {
        when (icon) {
            is ImageVector -> {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = if (isActive) Color.Gray else Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            is Int -> {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = contentDescription,
                    tint = if (isActive) Color.Gray else Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            else -> throw IllegalArgumentException("Tipo de ícone inválido: $icon")
        }
    }
}
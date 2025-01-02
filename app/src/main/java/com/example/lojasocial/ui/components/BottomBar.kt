package com.example.lojasocial.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lojasocial.R


@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onCheckInOutClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onDonationsClick: () -> Unit,
    onPortalClick: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier.height(64.dp),
        containerColor = Color(0xFFB71C1C)
    ) {
        BottomNavigationIcon(
            icon = Icons.Default.Home,
            contentDescription = "Home",
            onClick = onHomeClick
        )
        BottomNavigationIcon(
            icon = R.drawable.ic_checkin,
            contentDescription = "Check in/out",
            onClick = onCheckInOutClick
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
        BottomNavigationIcon(
            icon = R.drawable.ic_rateview,
            contentDescription = "Portal de Avaliação",
            onClick = onPortalClick
        )
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
                    tint = if (isActive) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            is Int -> {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = contentDescription,
                    tint = if (isActive) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            else -> throw IllegalArgumentException("Tipo de ícone inválido: $icon")
        }
    }
}
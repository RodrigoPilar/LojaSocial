package com.example.lojasocial.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lojasocial.R
import com.example.lojasocial.ui.components.BottomNavigationBar
import com.example.lojasocial.ui.components.TopBar


@Composable
fun HomeView(
    onCheckInOutClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onDonationsClick: () -> Unit = {},
    onPortalClick: () -> Unit = {},
    showPortal: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Botões de navegação
        NavigationButton(
            text = "Check in/out",
            iconId = R.drawable.ic_checkin,
            onClick = onCheckInOutClick
        )
        NavigationButton(
            text = "Calendário",
            iconId = R.drawable.ic_calendar,
            onClick = onCalendarClick
        )
        NavigationButton(
            text = "Doações",
            iconId = R.drawable.ic_donation,
            onClick = onDonationsClick
        )

        // Botão do Portal de Avaliação, exibido apenas se showPortal for true
        if (showPortal) {
            NavigationButton(
                text = "Portal de Avaliação",
                iconId = R.drawable.ic_rateview,
                onClick = onPortalClick
            )
        }
    }
}

@Composable
fun NavigationButton(
    text: String,
    iconId: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}






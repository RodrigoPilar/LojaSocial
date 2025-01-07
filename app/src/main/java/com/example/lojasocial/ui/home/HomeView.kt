package com.example.lojasocial.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    onCheckInClick: () -> Unit = {},
    onCheckOutClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onDonationsClick: () -> Unit = {},
    onPortalClick: () -> Unit = {},
    showPortal: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Botões de navegação
        NavigationButton(
            text = "Check In",
            iconId = R.drawable.ic_checkin,
            onClick = onCheckInClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botões de navegação
        NavigationButton(
            text = "Check Out",
            iconId = R.drawable.ic_checkout,
            onClick = onCheckOutClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        NavigationButton(
            text = "Calendário",
            iconId = R.drawable.ic_calendar,
            onClick = onCalendarClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        NavigationButton(
            text = "Doações",
            iconId = R.drawable.ic_donation,
            onClick = onDonationsClick
        )

        // Botão do Portal de Avaliação, exibido apenas se showPortal for true
        if (showPortal) {
            Spacer(modifier = Modifier.height(16.dp))
            NavigationButton(
                text = "Portal de Avaliação",
                iconId = R.drawable.ic_rateview,
                onClick = onPortalClick
            )
        }
        Spacer(modifier = Modifier.weight(1f))
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
            .height(80.dp)
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
                modifier = Modifier.size(34.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}






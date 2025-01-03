package com.example.lojasocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.ui.home.HomeView
import com.example.lojasocial.ui.components.BottomNavigationBar
import com.example.lojasocial.ui.components.TopBar
import com.example.lojasocial.ui.login.LoginView
import com.example.lojasocial.ui.theme.LojaSocialTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Desativa o modo noturno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContent {
            val navController = rememberNavController()
            LojaSocialTheme {
                // Obtemos o estado atual da rota fora do cálculo dos parâmetros
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                androidx.compose.material3.Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        // Mostra a TopBar apenas se não estivermos no Login
                        if (currentRoute != Screen.Login.route) {
                            TopBar(onUserClick = {navController.navigate(Screen.Login.route)})
                        }
                    },
                    bottomBar = {
                        // Mostra a BottomBar apenas se não estivermos no Login
                        if (currentRoute != Screen.Login.route) {
                            BottomNavigationBar(
                                onHomeClick = { navController.navigate(Screen.Home.route) },
                                onCheckInOutClick = { /* Ação Check-in/Out */ },
                                onCalendarClick = { /* Ação Calendário */ },
                                onDonationsClick = { /* Ação Doações */ },
                                onPortalClick = { /* Ação Portal */ }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Login.route) {
                            LoginView(onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            })
                        }

                        composable(Screen.Home.route) {
                            HomeView(
                                onCheckInOutClick = { /* Ação Check-in/Out */ },
                                onCalendarClick = { /* Ação Calendário */ },
                                onDonationsClick = { /* Ação Doações */ },
                                onPortalClick = { /* Ação Portal */ }
                            )
                        }
                    }

                    // Verifica se o utilizador já está autenticado
                    LaunchedEffect(Unit) {
                        val auth = Firebase.auth
                        val currentUser = auth.currentUser
                        if (currentUser != null) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }

    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
}

package com.example.lojasocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.ui.home.HomeView
import com.example.lojasocial.repositories.UserRepository
import com.example.lojasocial.ui.calendar.CalendarView
import com.example.lojasocial.ui.components.BottomNavigationBar
import com.example.lojasocial.ui.components.TopBar
import com.example.lojasocial.ui.donations.DonationsListView
import com.example.lojasocial.ui.donations.donationsView
import com.example.lojasocial.ui.login.LoginView
import com.example.lojasocial.ui.profile.ProfileView
import com.example.lojasocial.ui.registo.RegistoView
import com.example.lojasocial.ui.theme.LojaSocialTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Desativa o modo noturno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContent {
            val navController = rememberNavController()
            val userRepository = UserRepository() // Instância do UserRepository
            val userRole = remember { mutableStateOf("user") } // Valor padrão é "user"
            val auth = FirebaseAuth.getInstance()

            LaunchedEffect(Unit) {
                val role = userRepository.getUserRole() // Chama a função para buscar o role
                userRole.value = role
            }

            LojaSocialTheme {
                // Obtemos o estado atual da rota fora do cálculo dos parâmetros
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        // Mostra a TopBar apenas se não estivermos no Login ou Registo
                        if (currentRoute != Screen.Login.route && currentRoute != Screen.Registo.route) {
                            TopBar(
                                onProfileClick = {
                                    // Navegar para a página de perfil
                                    navController.navigate(Screen.Profile.route) {
                                        launchSingleTop = true
                                    }
                                },
                                onLogoutClick = {
                                    // Fazer logout e voltar à tela de login
                                    FirebaseAuth.getInstance().signOut()
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Home.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        // Mostra a BottomBar apenas se não estivermos no Login ou Registo
                        if (currentRoute != Screen.Login.route && currentRoute != Screen.Registo.route) {
                            BottomNavigationBar(
                                onHomeClick = { navController.navigate(Screen.Home.route) },
                                onCheckInOutClick = { /* Ação Check-in/Out */ },
                                onDonationsClick = { navController.navigate(Screen.Donations.route) },
                                onCalendarClick = { navController.navigate(Screen.Calendar.route) },
                                onPortalClick = { /* Ação Portal */ },
                                showPortal = (userRole.value == "admin")
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Página de login
                        composable(Screen.Login.route) {
                            LoginView(
                                onLoginSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onNavigateToRegisto = {
                                    navController.navigate(Screen.Registo.route)
                                }
                            )
                        }

                        // Página de registo
                        composable(Screen.Registo.route) {
                            RegistoView(
                                onRegistrationSuccess = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Registo.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Home.route) {
                            HomeView(
                                onCheckInOutClick = { /* Ação Check-in/Out */ },
                                onCalendarClick = { navController.navigate(Screen.Calendar.route) },
                                onDonationsClick = { navController.navigate(Screen.Donations.route) },
                                onPortalClick = { /* Ação Portal */ },
                                showPortal = (userRole.value == "admin")
                            )
                        }

                        composable(Screen.Profile.route) {
                            ProfileView()
                        }

                        // Página de doações
                        composable(Screen.Donations.route) {
                            donationsView(
                                isAdmin = userRole.value == "admin",
                                onConsultarDoacoesClick = {
                                    navController.navigate(Screen.DonationsList.route)
                                }
                            )
                        }

                        // Calendário
                        composable(Screen.Calendar.route) {
                            CalendarView(
                                onDaySelected = { selectedDate ->
                                    println("Selected date: $selectedDate")
                                },
                                selectedDates = listOf(
                                    LocalDate.of(2025, 1, 5), // Example date 1
                                    LocalDate.of(2025, 1, 10) // Example date 2
                                )
                            )
                        }

                        composable(Screen.DonationsList.route) {
                            DonationsListView()
                        }
                    }

                    DisposableEffect(Unit) {
                        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                            val user = firebaseAuth.currentUser
                            if (user != null) {
                                // Lança uma coroutine para chamar a função suspend
                                CoroutineScope(Dispatchers.IO).launch {
                                    val role = userRepository.getUserRole()
                                    userRole.value = role
                                }
                            } else {
                                userRole.value = "user" // Define como "user" se não houver utilizador autenticado
                            }
                        }

                        // Adiciona o listener
                        auth.addAuthStateListener(authListener)

                        // Remove o listener ao sair da composição
                        onDispose {
                            auth.removeAuthStateListener(authListener)
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Registo : Screen("registo")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Donations : Screen("donations")
    object DonationsList : Screen("donationsList")
    object Calendar : Screen("calendar")
}

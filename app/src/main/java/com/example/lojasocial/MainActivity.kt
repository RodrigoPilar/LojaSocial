package com.example.lojasocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.ui.home.HomeView
import com.example.lojasocial.repositories.UserRepository
import com.example.lojasocial.ui.check.CheckInView
import com.example.lojasocial.ui.check.CheckOutView
//import com.example.lojasocial.ui.calendar.CalendarView
import com.example.lojasocial.ui.components.BottomNavigationBar
import com.example.lojasocial.ui.components.TopBar
import com.example.lojasocial.ui.donations.DonationsListView
import com.example.lojasocial.ui.donations.donationsView
import com.example.lojasocial.ui.login.LoginView
import com.example.lojasocial.ui.profile.ProfileView
import com.example.lojasocial.ui.registo.RegistoView
import com.example.lojasocial.ui.theme.LojaSocialTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Desativa o modo noturno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // Inicializa o ThreeTenABP para suporte a LocalDate em API < 26
        AndroidThreeTen.init(this)

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

                androidx.compose.material3.Scaffold(
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
                                onCheckInClick = { navController.navigate(Screen.CheckIn.route) },
                                onCheckOutClick = { navController.navigate(Screen.CheckOut.route) },
                                onDonationsClick = { navController.navigate(Screen.Donations.route) },
                                onCalendarClick = { navController.navigate(Screen.Calendar.route)},
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
                                onCheckInClick = { navController.navigate(Screen.CheckIn.route) },
                                onCheckOutClick = { navController.navigate(Screen.CheckOut.route) },
                                onCalendarClick = { navController.navigate(Screen.Calendar.route) },
                                onDonationsClick = { navController.navigate(Screen.Donations.route) },
                                onPortalClick = { /* Ação Portal */ },
                                showPortal = (userRole.value == "admin")
                            )
                        }

                        composable(Screen.Profile.route) {
                            ProfileView()
                        }

                        composable(Screen.CheckIn.route) {
                            CheckInView()
                        }

                        composable(Screen.CheckOut.route) {
                            CheckOutView()
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
                        composable(Screen.DonationsList.route) {
                            DonationsListView()
                        }
                        /*composable(Screen.Calendar.route) {
                            CalendarView()
                        }*/
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
    object CheckIn : Screen("checkin")
    object CheckOut : Screen("checkout")

}

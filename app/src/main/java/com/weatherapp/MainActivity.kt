package com.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.weatherapp.api.WeatherService
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.ui.CityDialog
import com.weatherapp.ui.nav.BottomNavBar
import com.weatherapp.ui.nav.BottomNavItem
import com.weatherapp.ui.nav.MainNavHost
import com.weatherapp.ui.nav.Route
import com.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    // 🧬 Instancia os serviços fora do escopo de UI para evitar recriações em loop
    private val fbDB by lazy { FBDatabase() }
    private val weatherService by lazy { WeatherService(this) }
    private lateinit var viewModel: MainViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o ViewModel usando o provedor nativo da Activity
        val factory = MainViewModelFactory(fbDB, weatherService)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val items = listOf(
                BottomNavItem.HomeButton,
                BottomNavItem.ListButton,
                BottomNavItem.MapButton
            )

            val currentRoute = navController.currentBackStackEntryAsState()
            val showButton = currentRoute.value?.destination?.hasRoute(Route.List::class) == true

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { }
            )

            var showDialog by remember { mutableStateOf(false) }

            // 🌐 Prática 08 - Parte 3 - Passo 4: Escuta as mudanças de página do ViewModel e navega
            LaunchedEffect(viewModel.page) {
                navController.navigate(viewModel.page) {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) { saveState = true }
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            }

            WeatherAppTheme {
                if (showDialog) {
                    CityDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { city ->
                            if (city.isNotBlank()) {
                                viewModel.addCity(city)
                            }
                            showDialog = false
                        }
                    )
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                val name = viewModel.user?.name ?: "[carregando...]"
                                Text("Bem-vindo/a! $name")
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Sair"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavBar(viewModel = viewModel, navController = navController, items = items)
                    },
                    floatingActionButton = {
                        if (showButton) {
                            FloatingActionButton(onClick = { showDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar")
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        LaunchedEffect(Unit) {
                            launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        }

                        MainNavHost(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
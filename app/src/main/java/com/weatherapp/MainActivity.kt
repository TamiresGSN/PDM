package com.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.weatherapp.ui.CityDialog
import com.weatherapp.ui.nav.BottomNavItem
import com.weatherapp.ui.nav.BottomNavBar
import com.weatherapp.ui.nav.MainNavHost
import com.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Parte 2 - Passo 6: Instanciação do ViewModel (State Hoisting)
            val viewModel: MainViewModel by viewModels()

            // Configurações de Navegação
            val navController = rememberNavController()
            val items = listOf(
                BottomNavItem.HomeButton,
                BottomNavItem.ListButton,
                BottomNavItem.MapButton
            )

            // Parte 3 - Passo 2: Estado para controlar a exibição do Diálogo [cite: 165]
            var showDialog by remember { mutableStateOf(false) }

            WeatherAppTheme {
                // Parte 3 - Passo 3: Lógica para exibir o Diálogo de adição [cite: 166-174]
                if (showDialog) {
                    CityDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { city ->
                            if (city.isNotBlank()) {
                                viewModel.add(city) // Adiciona via ViewModel [cite: 172, 175]
                            }
                            showDialog = false
                        }
                    )
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Bem-vindo/a!") },
                            actions = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Sair"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavBar(navController = navController, items = items)
                    },
                    floatingActionButton = {
                        // Parte 3 - Passo 4: Botão "+" ativa o diálogo [cite: 176-178]
                        FloatingActionButton(onClick = { showDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Adicionar")
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        // Parte 2 - Passo 5: Repasse do ViewModel para o NavHost [cite: 125]
                        MainNavHost(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
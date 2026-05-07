package com.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
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
            // Passo 4 da Parte 2: Inicialização do NavController
            val navController = rememberNavController()

            // Passo 4 da Parte 2: Definição da lista de itens da navegação
            val items = listOf(
                BottomNavItem.HomeButton,
                BottomNavItem.ListButton,
                BottomNavItem.MapButton,
            )

            WeatherAppTheme {
                // Passo 4 da Parte 2: Configuração do Scaffold (Estrutura da Tela)
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Bem-vindo/a!") },
                            actions = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Sair do App"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        // Passo 4 da Parte 2: Inclusão da barra inferior personalizada
                        BottomNavBar(navController = navController, items = items)
                    },
                    floatingActionButton = {
                        // Passo 4 da Parte 2: Adição do Botão Flutuante
                        FloatingActionButton(onClick = { /* Ação futuramente */ }) {
                            Icon(Icons.Default.Add, contentDescription = "Adicionar")
                        }
                    }
                ) { innerPadding ->
                    // Passo 4 da Parte 2: Conteúdo principal onde as telas são exibidas
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        MainNavHost(navController = navController)
                    }
                }
            }
        }
    }
}
package com.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weatherapp.MainViewModel
import com.weatherapp.ui.HomePage
import com.weatherapp.ui.ListPage
import com.weatherapp.ui.MapPage

@Composable
fun MainNavHost(
    navController: NavHostController,
    viewModel: MainViewModel, // Passo 5 da Parte 2: Agora recebe o ViewModel aqui
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home
    ) {
        // Passo 5 da Parte 2: Repassando o viewModel para cada uma das páginas
        composable<Route.Home> {
            HomePage(modifier = modifier, viewModel = viewModel)
        }
        composable<Route.List> {
            ListPage(modifier = modifier, viewModel = viewModel)
        }
        composable<Route.Map> {
            MapPage(modifier = modifier, viewModel = viewModel)
        }
    }
}
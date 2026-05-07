package com.weatherapp.model

// Passo 1 da Parte 1: Classe que representa uma cidade favorita
data class City(
    val name: String,
    val weather: String? = null,
    val location: String? = null
)
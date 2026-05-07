package com.weatherapp

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.weatherapp.model.City

// Parte 2 - Passo 1: Classe que gerencia os dados da lista [cite: 96-97]
class MainViewModel : ViewModel() {

    // Passo 2 da Parte 2: Função renomeada para 'gerarCidades' para evitar conflito [cite: 109]
    private fun gerarCidades() = List(20) { i ->
        City(name = "Cidade $i", weather = "Carregando clima...")
    }

    // Lista privada que pode ser alterada internamente [cite: 98]
    private val _cities = gerarCidades().toMutableStateList()

    // Lista exposta para a UI (O Kotlin cria um 'getCities' automático aqui) [cite: 99-100]
    val cities: List<City>
        get() = _cities

    // Função para remover uma cidade da lista [cite: 101-103]
    fun remove(city: City) {
        _cities.remove(city)
    }

    // Função para adicionar uma nova cidade à lista [cite: 104-107]
    fun add(name: String) {
        _cities.add(City(name = name))
    }
}
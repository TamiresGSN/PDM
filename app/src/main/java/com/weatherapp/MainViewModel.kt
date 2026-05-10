package com.weatherapp

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.model.City

// Parte 2 - Passo 1: Classe que gerencia os dados da lista
class MainViewModel : ViewModel() {

    // Passo 2 da Parte 2: Função renomeada para 'gerarCidades' para evitar conflito
    private fun gerarCidades() = List(20) { i ->
        City(name = "Cidade $i", weather = "Carregando clima...")
    }

    // Lista privada que pode ser alterada internamente
    private val _cities = gerarCidades().toMutableStateList()

    // Lista exposta para a UI (O Kotlin cria um 'getCities' automático aqui)
    val cities: List<City>
        get() = _cities

    // Função para remover uma cidade da lista
    fun remove(city: City) {
        _cities.remove(city)
    }

    // Função para adicionar uma nova cidade à lista
    fun add(name: String, location: LatLng? = null) {
        _cities.add(City(name = name, location = location))
    }
}
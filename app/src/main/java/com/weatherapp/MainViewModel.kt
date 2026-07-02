package com.weatherapp

import android.annotation.TargetApi
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.api.WeatherService
import com.weatherapp.api.toForecast
import com.weatherapp.api.toWeather
import com.weatherapp.db.fb.FBCity
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.FBUser
import com.weatherapp.db.fb.toFBCity
import com.weatherapp.model.City
import com.weatherapp.model.Forecast
import com.weatherapp.model.User
import com.weatherapp.model.Weather
import com.weatherapp.ui.nav.Route

class MainViewModel(
    private val db: FBDatabase,
    private val service: WeatherService
) : ViewModel(), FBDatabase.Listener {

    private val _cities = mutableStateListOf<City>()
    val cities: List<City>
        get() = _cities.toList()

    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    // ── Prática 08 - Parte 1 (Passos 7 a 9): Clima atual ─────────────────
    private val _weather = mutableStateMapOf<String, Weather>()

    fun weather(name: String) = _weather.getOrPut(name) {
        loadWeather(name)
        Weather.LOADING // retorno enquanto carrega em background
    }

    private fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            apiWeather?.let {
                _weather[name] = it.toWeather()
                // Prática 09 - Parte 2 - Passo 3: dispara a carga do bitmap
                loadBitmap(name)
            }
        }
    }

    // ── Prática 09 - Parte 2 (Passo 3): carrega o bitmap do clima ────────
    private fun loadBitmap(name: String) {
        _weather[name]?.let { weather ->
            service.getBitmap(weather.imgUrl) { bitmap ->
                _weather[name] = weather.copy(bitmap = bitmap)
            }
        }
    }

    // ── Prática 08 - Parte 2 (Passos 5 e 6): Previsão do tempo ───────────
    private val _forecast = mutableStateMapOf<String, List<Forecast>?>()

    fun forecast(name: String) = _forecast.getOrPut(name) {
        loadForecast(name)
        emptyList() // retorno enquanto carrega em background
    }

    private fun loadForecast(name: String) {
        service.getForecast(name) { apiForecast ->
            apiForecast?.let {
                _forecast[name] = it.toForecast()
            }
        }
    }

    // ── Prática 08 - Parte 2 (Passo 7): Cidade selecionada ───────────────
    private var _city = mutableStateOf<String?>(null)
    var city: String?
        get() = _city.value
        set(tmp) { _city.value = tmp }

    // ── Prática 08 - Parte 3 (Passo 1): Página atual (navegação) ─────────
    private var _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }

    init {
        db.setListener(this)
    }

    fun remove(city: City) {
        db.remove(city.toFBCity())
    }

    fun add(name: String, location: LatLng? = null) {
        db.add(City(name = name, location = location).toFBCity())
    }

    // Prática 07: Adiciona cidade pelo Nome (Busca coordenadas na API antes de salvar)
    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(City(name = name, location = LatLng(lat, lng)).toFBCity())
            }
        }
    }

    // Prática 07: Adiciona cidade pelo Clique no Mapa (Busca o Nome na API antes de salvar)
    fun addCity(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(City(name = name, location = location).toFBCity())
            }
        }
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        _user.value = null
        _cities.clear()
    }

    override fun onCityAdded(city: FBCity) {
        _cities.add(city.toCity())
    }

    override fun onCityUpdated(city: FBCity) {
        val cityName = city.name ?: return
        val index = _cities.indexOfFirst { it.name == cityName }
        if (index != -1) {
            _cities[index] = city.toCity()
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun onCityRemoved(city: FBCity) {
        _cities.remove(city.toCity())
    }

    override fun onCleared() {
        super.onCleared()
        db.setListener(null)
    }
}

class MainViewModelFactory(
    private val db: FBDatabase,
    private val service: WeatherService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db, service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
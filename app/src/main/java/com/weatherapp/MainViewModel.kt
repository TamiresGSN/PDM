package com.weatherapp

import android.annotation.TargetApi
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.db.fb.FBCity
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.FBUser
import com.weatherapp.db.fb.toFBCity
import com.weatherapp.model.City
import com.weatherapp.model.User

class MainViewModel(private val db: FBDatabase) : ViewModel(), FBDatabase.Listener {

    private val _cities = mutableStateListOf<City>()
    val cities: List<City>
        get() = _cities.toList()

    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    init {
        db.setListener(this)
    }

    fun remove(city: City) {
        db.remove(city.toFBCity())
    }

    fun add(name: String, location: LatLng? = null) {
        db.add(City(name = name, location = location).toFBCity())
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

class MainViewModelFactory(private val db: FBDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
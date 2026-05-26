package com.weatherapp

import android.app.Application
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class WeatherApp : Application() {

    private val FLAGS = Intent.FLAG_ACTIVITY_SINGLE_TOP or
            Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_CLEAR_TASK

    override fun onCreate() {
        super.onCreate()

        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                goToMain()
            } else {
                goToLogin()
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java).setFlags(FLAGS)
        this.startActivity(intent)
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java).setFlags(FLAGS)
        this.startActivity(intent)
    }
}
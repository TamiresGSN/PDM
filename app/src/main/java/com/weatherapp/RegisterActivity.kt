package com.weatherapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.toFBUser
import com.weatherapp.model.User
import com.weatherapp.ui.DataField
import com.weatherapp.ui.PasswordField
import com.weatherapp.ui.theme.WeatherAppTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RegisterPage(modifier: Modifier = Modifier) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val activity = LocalContext.current as Activity

    Column(
        modifier = modifier.padding(24.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Crie sua conta", fontSize = 24.sp)
        Spacer(modifier = Modifier.size(16.dp))

        DataField(label = "Nome", value = name, onValueChange = { name = it })
        Spacer(modifier = Modifier.size(8.dp))

        DataField(label = "E-mail", value = email, onValueChange = { email = it })
        Spacer(modifier = Modifier.size(8.dp))

        PasswordField(label = "Senha", value = password, onValueChange = { password = it })
        Spacer(modifier = Modifier.size(8.dp))

        PasswordField(label = "Repetir Senha", value = confirmPassword, onValueChange = { confirmPassword = it })

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.91f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    com.google.firebase.auth.FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                FBDatabase().register(User(name, email).toFBUser())

                                Toast.makeText(activity, "Registro OK!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(activity, "Registro FALHOU! " + task.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                },
                enabled = name.isNotEmpty() && email.isNotEmpty() &&
                        password.isNotEmpty() && password == confirmPassword
            ) {
                Text("Registrar")
            }

            Button(onClick = {
                name = ""; email = ""; password = ""; confirmPassword = ""
            }) {
                Text("Limpar")
            }
        }
    }
}
package com.weatherapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.ui.*
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

        // Passo 1 (Desafio): Utilizando DataField para Nome e E-mail
        DataField(label = "Nome", value = name, onValueChange = { name = it })
        Spacer(modifier = Modifier.size(8.dp))

        DataField(label = "E-mail", value = email, onValueChange = { email = it })
        Spacer(modifier = Modifier.size(8.dp))

        // Passo 1 (Desafio): Utilizando PasswordField para as senhas
        PasswordField(label = "Senha", value = password, onValueChange = { password = it })
        Spacer(modifier = Modifier.size(8.dp))

        PasswordField(label = "Repetir Senha", value = confirmPassword, onValueChange = { confirmPassword = it })

        Spacer(modifier = Modifier.size(16.dp))

        // Organização dos botões Registrar e Limpar
        Row(
            modifier = Modifier.fillMaxWidth(0.91f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    Toast.makeText(activity, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    activity.finish()
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
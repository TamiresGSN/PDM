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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.ui.theme.WeatherAppTheme
import android.content.Intent
import com.weatherapp.ui.*


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Passo 4: Chamando a LoginPage
                    LoginPage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPage(modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val activity = LocalContext.current as Activity

    // Passo 6: Ajuste de alinhamento vertical
    Column(
        modifier = modifier.padding(24.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Passo 5: Fazendo os campos usarem 91% da largura da tela
        val modifierField = modifier.fillMaxWidth(fraction = 0.91f)

        Text(
            text = "Bem-vindo/a!",
            fontSize = 24.sp
        )
        // Passo 7: Adicionado espaçador entre o título e o campo
        Spacer(modifier = Modifier.size(12.dp))

        DataField(
            label = "Digite seu e-mail",
            value = email,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.size(12.dp))

        PasswordField(
            label = "Digite sua senha",
            value = password,
            onValueChange = { password = it }
        )

        // Passo 7: Adicionado espaçador entre o campo e os botões
        Spacer(modifier = Modifier.size(12.dp))

        // Passo 6: Ajustado o horizontalArrangement para melhor posicionar os botões
        Row(
            modifier = modifierField.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    Toast.makeText(activity, "Login OK!", Toast.LENGTH_LONG).show()
                    // Passo 2 da Parte 3: Dispara o Intent para chamar a MainActivity
                    activity.startActivity(
                        Intent(activity, MainActivity::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                        )
                    )
                },
                enabled = email.isNotEmpty() && password.isNotEmpty()
            ) {
                Text("Login")
            }

            // Passo 7: Espaço entre os botões
            Spacer(modifier = Modifier.size(12.dp))

            Button(
                onClick = { email = ""; password = "" }
            ) {
                Text("Limpar")
            }

        }

        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = {
                activity.startActivity(Intent(activity, RegisterActivity::class.java))
            },
            modifier = modifierField
        ) {
            Text("Cadastrar-se")
        }
    }
}
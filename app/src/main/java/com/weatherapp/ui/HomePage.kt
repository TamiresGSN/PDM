package com.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weatherapp.MainViewModel
import com.weatherapp.R
import com.weatherapp.model.Forecast
import java.text.DecimalFormat

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (viewModel.city == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = "Selecione uma cidade!",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp
                )
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                // Prática 09 - Parte 1 - Passo 4: imagem do clima atual
                AsyncImage(
                    model = viewModel.weather(viewModel.city!!).imgUrl,
                    modifier = Modifier.size(140.dp),
                    error = painterResource(id = R.drawable.loading),
                    contentDescription = "Imagem"
                )
                Column {
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(text = viewModel.city ?: "Selecione uma cidade...", fontSize = 28.sp)

                    viewModel.city?.let { name ->
                        val weatherData = viewModel.weather(name)
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(text = weatherData.desc ?: "...", fontSize = 22.sp)
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(text = "Temp: ${weatherData.temp}°C", fontSize = 22.sp)
                    }
                }
            }

            viewModel.forecast(viewModel.city!!)?.let { forecasts ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(forecasts) { forecast ->
                        ForecastItem(forecast = forecast, onClick = {})
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastItem(
    forecast: Forecast,
    modifier: Modifier = Modifier,
    onClick: (Forecast) -> Unit
) {
    val format = DecimalFormat("#.0")
    val tempMin = format.format(forecast.tempMin)
    val tempMax = format.format(forecast.tempMax)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = { onClick(forecast) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prática 09 - Parte 1 - Passo 5: imagem da previsão
        AsyncImage(
            model = forecast.imgUrl,
            modifier = Modifier.size(70.dp),
            error = painterResource(id = R.drawable.loading),
            contentDescription = "Imagem"
        )
        Spacer(modifier = Modifier.size(16.dp))
        Column {
            Text(text = forecast.weather, fontSize = 24.sp)
            Row {
                Text(text = forecast.date, fontSize = 20.sp)
                Spacer(modifier = Modifier.size(12.dp))
                Text(text = "Min: $tempMin°C", fontSize = 16.sp)
                Spacer(modifier = Modifier.size(12.dp))
                Text(text = "Max: $tempMax°C", fontSize = 16.sp)
            }
        }
    }
}
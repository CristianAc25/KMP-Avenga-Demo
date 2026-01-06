package com.avenga.kmpdemo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import kmpcompanydemo.composeapp.generated.resources.Res
import kmpcompanydemo.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val deviceMonitor = remember { DeviceMonitor() }
        val info = remember { deviceMonitor.getSystemData() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SYSTEM MONITOR",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF00FFCC)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { showContent = !showContent }) {
                Text(if (showContent) "Ocultar Datos" else "Escanear Sistema")
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(showContent) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow("OS:", info.osName)
                        InfoRow("Versión:", info.osVersion)
                        InfoRow("Modelo:", info.deviceModel)
                        InfoRow("Pantalla:", info.screenResolution)

                        Spacer(modifier = Modifier.height(10.dp))
                        Image(
                            painterResource(Res.drawable.compose_multiplatform),
                            null,
                            modifier = Modifier.align(Alignment.CenterHorizontally).size(50.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, color = Color.Gray, modifier = Modifier.weight(1f))
        Text(text = value, color = Color.White, modifier = Modifier.weight(2f))
    }
}
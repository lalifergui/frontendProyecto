package com.example.pantallas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pantallas.ui.login.PantallaLogin // Asegúrate de importar tu Login
import com.example.pantallas.ui.registro.Registrar
import com.example.pantallas.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Reemplaza "AppTheme" por el nombre que tenga tu función en Theme.kt
            AppTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaLogin(
                        onRegisterClick = {
                            val intento = Intent(this, Registrar::class.java)
                            startActivity(intento)
                        }
                    )
                }
            }
        }
    }
}
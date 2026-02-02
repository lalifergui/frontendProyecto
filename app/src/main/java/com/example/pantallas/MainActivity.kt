package com.example.pantallas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pantallas.ui.login.PantallaLogin // Asegúrate de importar tu Login
import com.example.pantallas.ui.registro.Registrar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Llamamos a la pantalla de Login aquí
            PantallaLogin(
                onRegisterClick = {
                    // Lógica para ir al registro desde el Main
                    val intento = Intent(this, Registrar::class.java)
                    startActivity(intento)
                }
            )
        }
    }
}
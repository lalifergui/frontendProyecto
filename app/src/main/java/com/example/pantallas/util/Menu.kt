package com.example.pantallas.util

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pantallas.ui.biblioteca.Biblioteca
import com.example.pantallas.ui.favoritos.Favoritos
import com.example.pantallas.ui.perfil.Perfil
import com.example.pantallas.ui.principal.Principal

@Composable
fun Menu(context: Context, usuarioId: Long) {

    // 1. ESTADO PARA LA ALERTA DEL CHAT
    var mostrarAlertaMensaje by remember { mutableStateOf(false) }

    // 2. LÓGICA DEL POP-UP (ALERT DIALOG)
    if (mostrarAlertaMensaje) {
        AlertDialog(
            onDismissRequest = { mostrarAlertaMensaje = false },
            title = { Text(text = "El chat NO está disponible") },
            text = { Text(text = "Si quieres tener la opción de chatear debes obtener el plan premium.") },
            confirmButton = {
                TextButton(onClick = { mostrarAlertaMensaje = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(56.dp)
    ) {
        // --- BOTÓN HOME ---
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = false,
            onClick = {
                val intent = Intent(context, Principal::class.java)
                intent.putExtra("USUARIO_ID", usuarioId)
                context.startActivity(intent)
            }
        )

        // --- BOTÓN FAVORITOS ---
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") },
            selected = false,
            onClick = {
                val intent = Intent(context, Favoritos::class.java)
                intent.putExtra("USUARIO_ID", usuarioId)
                context.startActivity(intent)
            }
        )

        // --- BOTÓN CHAT (NUEVO) ---
        NavigationBarItem(
            icon = { Icon(Icons.Filled.MailOutline, contentDescription = "Chat") },
            selected = false,
            onClick = {
                // Al hacer click, activamos la alerta en lugar de navegar
                mostrarAlertaMensaje = true
            }
        )

        /**
         *    // --- BOTÓN BIBLIOTECA ---
         *         NavigationBarItem(
         *             icon = { Icon(Icons.Filled.MenuBook, contentDescription = "Biblioteca") },
         *             selected = false,
         *             onClick = {
         *                 val intent = Intent(context, Biblioteca::class.java)
         *                 intent.putExtra("USUARIO_ID", usuarioId)
         *                 context.startActivity(intent)
         *             }
         *         )
         */

        // --- BOTÓN PERFIL ---
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            selected = false,
            onClick = {
                val intent = Intent(context, Perfil::class.java)
                intent.putExtra("USUARIO_ID", usuarioId)
                context.startActivity(intent)
            }
        )
    }
}
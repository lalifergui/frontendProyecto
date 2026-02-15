package com.example.pantallas.util

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pantallas.ui.biblioteca.Biblioteca
import com.example.pantallas.ui.favoritos.Favoritos
import com.example.pantallas.ui.login.Login
import com.example.pantallas.ui.perfil.Perfil
import com.example.pantallas.ui.principal.Principal
@Composable
fun Menu(context: Context, usuarioId: Long) {

    // 1. ESTADOS PARA LAS ALERTAS
    var mostrarAlertaMensaje by remember { mutableStateOf(false) }
    var mostrarAlertaCerrarSesion by remember { mutableStateOf(false) } // 🎯 Nuevo estado

    // 2. LÓGICA DEL POP-UP DEL CHAT
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

    // 3. 🎯 LÓGICA DEL POP-UP DE CERRAR SESIÓN
    if (mostrarAlertaCerrarSesion) {
        AlertDialog(
            onDismissRequest = { mostrarAlertaCerrarSesion = false },
            title = { Text(text = "Cerrar sesión") },
            text = { Text(text = "¿Estás seguro de que quieres cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarAlertaCerrarSesion = false
                        // Navegamos al Login y limpiamos el historial de pantallas
                        val intent = Intent(context, Login::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                ) {
                    Text("Sí, salir", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarAlertaCerrarSesion = false }) {
                    Text("No, quedarme")
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

        // --- BOTÓN CHAT ---
        NavigationBarItem(
            icon = { Icon(Icons.Filled.MailOutline, contentDescription = "Chat") },
            selected = false,
            onClick = { mostrarAlertaMensaje = true }
        )

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

        // --- BOTÓN CERRAR SESIÓN CORREGIDO ---
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Output, contentDescription = "Cerrar Sesión") },
            selected = false,
            onClick = {
                // En lugar de navegar directo, mostramos el diálogo
                mostrarAlertaCerrarSesion = true
            }
        )
    }
}
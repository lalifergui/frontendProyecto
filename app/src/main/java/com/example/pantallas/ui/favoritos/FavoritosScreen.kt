package com.example.pantallas.ui.favoritos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pantallas.util.Menu

class Favoritos : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Muestra tu pantalla aquí cuando ejecutes la app
            PantallaFavoritos()
        }
    }
}
@Composable
fun PantallaFavoritos(
    //una funcion no recibe parámetros y no devuelve nada
    onRegistrarClick: () -> Unit = {}
) {
    val context = LocalContext.current
    // Eliminamos 'nombre' ya que no lo estamos usando en este diseño
    // var nombre by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp) // Reducimos el padding vertical total y ajustamos el Spacer
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(26.dp)) // Ajuste para el espacio superior

        // 1. Título "Favoritos"
        Text(
            text = "Favoritos",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Barra de Navegación (Usuarios | Notificaciones)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp) // Padding para que no toque los bordes
                .border(
                    width = 1.dp,
                    color = Color(0xFFCCCCCC), // Un gris suave para el borde
                    shape = RoundedCornerShape(8.dp)
                )
                .height(56.dp), // Altura fija para la barra
            horizontalArrangement = Arrangement.SpaceEvenly, // Distribuye el espacio uniformemente
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Botón/Opción de Usuarios
            IconButton(
                onClick = { /* TODO: Navegar a la sección de Usuarios */ },
                modifier = Modifier.weight(1f) // Ocupa la mitad del espacio disponible
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Usuarios",
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFF4285F4) // Color azul de Google/algo distintivo
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Usuarios", fontSize = 16.sp, color = Color.Black)
                }
            }

            // Separador (Divider)
            Divider(
                color = Color(0xFFCCCCCC), // Mismo gris suave
                modifier = Modifier
                    .height(32.dp) // Altura del separador
                    .padding(vertical = 4.dp)
                    .width(1.dp) // Ancho del separador
            )

            // Botón/Opción de Notificaciones
            IconButton(
                onClick = { /* TODO: Navegar a la sección de Notificaciones */ },
                modifier = Modifier.weight(1f) // Ocupa la otra mitad del espacio disponible
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificaciones",
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFFF4B400) // Color amarillo/naranja
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Notificaciones", fontSize = 16.sp, color = Color.Black)
                }
            }
        }

        // El contenido principal de la pantalla de Favoritos iría aquí (libros, bibliotecas favoritas, etc.)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(24.dp)
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "Aquí irá el contenido.",
                modifier = Modifier.align(Alignment.Center),
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Menú de navegación inferior (asumo que es tu componente Menu)
        Menu(context)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegistroScreenFav() {
    PantallaFavoritos ()
}
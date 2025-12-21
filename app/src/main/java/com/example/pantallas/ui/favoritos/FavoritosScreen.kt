package com.example.pantallas.ui.favoritos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.modelos.NotificacionesFavoritos
import com.example.pantallas.modelos.UsuariosFavoritos
import com.example.pantallas.util.CardPerfil
import com.example.pantallas.util.Menu

class Favoritos : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            PantallaFavoritos()
        }
    }
}

@Composable
fun PantallaFavoritos(

    viewModel: FavoritosViewModel = viewModel()
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Favoritos",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFFCCCCCC),
                    shape = RoundedCornerShape(8.dp)
                )
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Usuarios
            IconButton(
                onClick = { viewModel.cambiarPestaña("usuarios") },
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Usuarios",
                        modifier = Modifier.size(24.dp),
                        tint = if (viewModel.pestañaActual == "usuarios") Color(0xFF4285F4) else Color.Gray
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Usuarios",
                        fontSize = 16.sp,
                        color = if (viewModel.pestañaActual == "usuarios") Color.Black else Color.Gray
                    )
                }
            }

            Divider(
                color = Color(0xFFCCCCCC),
                modifier = Modifier
                    .height(32.dp)
                    .width(1.dp)
            )

            // Botón Notificaciones
            IconButton(
                onClick = { viewModel.cambiarPestaña("notificaciones") },
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificaciones",
                        modifier = Modifier.size(24.dp),
                        tint = if (viewModel.pestañaActual == "notificaciones") Color(0xFFF4B400) else Color.Gray
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Notificaciones",
                        fontSize = 16.sp,
                        color = if (viewModel.pestañaActual == "notificaciones") Color.Black else Color.Gray
                    )
                }
            }
        }

        // 3. Contenido Principal Dinámico
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp) // Altura fija para el contenedor de listas
                .padding(24.dp)
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(8.dp))
        ) {
            when (viewModel.pestañaActual) {
                "usuarios" -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(viewModel.listaFavoritos) { usuario ->
                            ItemUsuario(usuario)
                        }
                    }
                }

                "notificaciones" -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(viewModel.listaNotificaciones) { nota ->
                            ItemNotificacion(nota)
                        }
                    }
                }

                else -> {
                    // PANTALLA EN BLANCO (Estado inicial)
                    Text(
                        text = "Selecciona una opción para comenzar",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Menu(context)
    }
}

@Composable
fun ItemUsuario(usuarioFavorito: UsuariosFavoritos) {
    // Estado para expandir/colapsar la CardPerfil
    var estaExpandido by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { estaExpandido = !estaExpandido } // Interacción de click
    ) {
        if (estaExpandido) {
            // Muestra la tarjeta detallada usando tu componente CardPerfil
            CardPerfil(perfil = usuarioFavorito.perfil)
        } else {
            // Fila simple inicial
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE0E0E0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = usuarioFavorito.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(text = "Toca para ver detalles", fontSize = 11.sp, color = Color.Gray)
                }
            }
            Divider(modifier = Modifier.padding(horizontal = 12.dp), thickness = 0.5.dp)
        }
    }
}

@Composable
fun ItemNotificacion(nota: NotificacionesFavoritos) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = Color(0xFFF4B400),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "${nota.nombreUsuario} ${nota.mensaje}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(text = nota.fecha, fontSize = 11.sp, color = Color.Gray)
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegistroScreenFav() {
    PantallaFavoritos()
}
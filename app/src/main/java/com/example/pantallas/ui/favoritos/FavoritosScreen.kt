package com.example.pantallas.ui.favoritos

import android.content.Intent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantallas.modelos.NotificacionesFavoritos
import com.example.pantallas.modelos.UsuariosFavoritos
import com.example.pantallas.ui.perfil.Perfil
import com.example.pantallas.ui.theme.AppTheme
import com.example.pantallas.util.CardPerfil
import com.example.pantallas.util.Menu

class Favoritos : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperamos el ID del usuario logueado
        val usuarioId = intent.getLongExtra("USUARIO_ID", -1L)

        enableEdgeToEdge()
        setContent {
            // 1. Aplicas tu tema personalizado (asegúrate de que el nombre coincide con Theme.kt)
            AppTheme(dynamicColor = false) {
                // 2. Surface asegura que el fondo sea el de tu tema (ej. el color que elegiste en el generador)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaFavoritos(usuarioIdRecibido = usuarioId)
                }
            }
        }
    }
}

@Composable
fun PantallaFavoritos(
    usuarioIdRecibido: Long,
    viewModel: FavoritosViewModel = viewModel()
) {
    val context = LocalContext.current

    // Importar esto arriba: import androidx.lifecycle.Lifecycle
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val state by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    /**
     *  LaunchedEffect(lifecycleState) {
     *         if (lifecycleState == Lifecycle.State.RESUMED && usuarioIdRecibido != -1L) {
     *             viewModel.cargarFavoritos(usuarioIdRecibido)
     *         }
     *     }
     */
    LaunchedEffect(state) {
        if (state == Lifecycle.State.RESUMED && usuarioIdRecibido != -1L) {
            viewModel.cargarFavoritos(usuarioIdRecibido)
        }
    }
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

        // --- SELECTOR DE PESTAÑAS (Usuarios / Notificaciones) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.cambiarPestaña("usuarios") },
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Usuarios",
                        tint = if (viewModel.pestañaActual == "usuarios") Color(0xFF4285F4) else Color.Gray
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Usuarios",
                        color = if (viewModel.pestañaActual == "usuarios") Color.Black else Color.Gray
                    )
                }
            }

            Divider(color = Color(0xFFCCCCCC), modifier = Modifier.height(32.dp).width(1.dp))

            IconButton(
                onClick = { viewModel.cambiarPestaña("notificaciones") },
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificaciones",
                        tint = if (viewModel.pestañaActual == "notificaciones") Color(0xFFF4B400) else Color.Gray
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Notificaciones",
                        color = if (viewModel.pestañaActual == "notificaciones") Color.Black else Color.Gray
                    )
                }
            }
        }

        // --- CONTENEDOR DE LISTAS ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .padding(24.dp)
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(8.dp))
        ) {
            when (viewModel.pestañaActual) {
                "usuarios" -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(viewModel.listaFavoritos) { usuario ->
                            ItemUsuario(usuarioFavorito = usuario, miId = usuarioIdRecibido)
                        }
                    }
                }
                "notificaciones" -> {
                    // 🎯 AÑADIMOS COLUMNA PARA EL TEXTO PREMIUM
                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(viewModel.listaNotificaciones) { nota ->
                                ItemNotificacion(nota)
                            }
                        }

                        // MENSAJE PLAN PREMIUM
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFF9C4)) // Un fondo amarillo suave de advertencia
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Si quieres ver las notificaciones de tus usuarios favoritos debes acceder al plan premium",
                                color = Color.DarkGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        text = "Selecciona una opción",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Menu(context, usuarioIdRecibido)
    }
}

@Composable
fun ItemUsuario(usuarioFavorito: UsuariosFavoritos, miId: Long) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                // Al pulsar, navegamos al Perfil enviando los IDs necesarios
                val intent = Intent(context, Perfil::class.java).apply {
                    putExtra("USUARIO_ID", usuarioFavorito.id) // ID del ajeno
                    putExtra("MI_PROPIO_ID", miId) // Tu ID
                }
                context.startActivity(intent)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = usuarioFavorito.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Toca para ver su biblioteca", fontSize = 11.sp, color = Color.Gray)
            }
        }
        Divider(modifier = Modifier.padding(horizontal = 12.dp), thickness = 0.5.dp)
    }
}

@Composable
fun ItemNotificacion(nota: NotificacionesFavoritos) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFFF4B400))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = "${nota.nombreUsuario} ${nota.mensaje}", fontSize = 14.sp)
            Text(text = nota.fecha, fontSize = 11.sp, color = Color.Gray)
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
}
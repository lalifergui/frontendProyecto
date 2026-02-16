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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.*
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
import androidx.room.Delete
import com.example.pantallas.modelos.NotificacionesFavoritos
import com.example.pantallas.modelos.UsuariosFavoritos
import com.example.pantallas.ui.perfil.Perfil
import com.example.pantallas.ui.theme.AppTheme
import com.example.pantallas.util.Menu

class Favoritos : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usuarioId = intent.getLongExtra("USUARIO_ID", -1L)

        enableEdgeToEdge()
        setContent {
            AppTheme(dynamicColor = false) {
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
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

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

        // --- SELECTOR DE PESTAÑAS ---
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

            VerticalDivider(modifier = Modifier.height(32.dp), color = Color(0xFFCCCCCC))

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
                            // 🎯 Pasamos el viewModel para gestionar la eliminación
                            ItemUsuario(
                                usuarioFavorito = usuario,
                                miId = usuarioIdRecibido,
                                viewModel = viewModel
                            )
                        }
                    }
                }
                "notificaciones" -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(viewModel.listaNotificaciones) { nota ->
                                ItemNotificacion(nota)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFF9C4))
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
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Menu(context, usuarioIdRecibido)
    }
}

@Composable
fun ItemUsuario(
    usuarioFavorito: UsuariosFavoritos,
    miId: Long,
    viewModel: FavoritosViewModel
) {
    val context = LocalContext.current
    // Estado para el diálogo de confirmación de borrado
    var mostrarConfirmacionBorrar by remember { mutableStateOf(false) }

    // --- 1. DIÁLOGO DE CONFIRMACIÓN DE ELIMINACIÓN ---
    if (mostrarConfirmacionBorrar) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionBorrar = false },
            title = { Text(text = "Eliminar favorito", fontWeight = FontWeight.Bold) },
            text = { Text(text = "¿Estás seguro de que quieres eliminar a ${usuarioFavorito.nombre} de tu lista de favoritos?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacionBorrar = false
                        viewModel.eliminarFavorito(miId, usuarioFavorito.id)
                    }
                ) {
                    Text("Sí, eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacionBorrar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
        // Ya no es clickable toda la fila para evitar errores
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar circular
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Nombre del usuario (ocupa el espacio central)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = usuarioFavorito.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(text = "Favorito", fontSize = 11.sp, color = Color.Gray)
            }

            // --- 2. ICONO VER PERFIL (EL OJO) ---
            IconButton(onClick = {
                val intent = Intent(context, Perfil::class.java).apply {
                    putExtra("USUARIO_ID", usuarioFavorito.id)
                    putExtra("MI_PROPIO_ID", miId)
                }
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Filled.RemoveRedEye,
                    contentDescription = "Ver Perfil",
                    tint = Color(0xFF4285F4) // Azul
                )
            }

            // --- 3. ICONO ELIMINAR (LA BASURA) ---
            IconButton(onClick = { mostrarConfirmacionBorrar = true }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFEA4335) // Rojo
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), thickness = 0.5.dp)
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
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
}
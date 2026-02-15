package com.example.pantallas.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pantallas.modelos.Perfil

/**
 * Componente reutilizable para mostrar la "Carta de Presentación" del usuario.
 * Se usa en Perfil, Editar Perfil y Perfil Ajeno (Favoritos).
 */
@Composable
fun CardPerfil(perfil: Perfil, foto: String? = null) {

    val FondoCardColor = MaterialTheme.colorScheme.primary
    val TextoCardColor = MaterialTheme.colorScheme.onPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(FondoCardColor, shape = RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 1. Círculo de Foto de Perfil
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, TextoCardColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (!foto.isNullOrEmpty()) {
                // Imagen real con Coil
                AsyncImage(
                    model = foto,
                    contentDescription = "Foto de perfil de ${perfil.nombre}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Evita que la foto se estire
                )
            } else {
                // Icono por defecto si no hay foto
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Sin foto",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 2. Columna de Datos Personales
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${perfil.nombre} ${perfil.apellidos}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextoCardColor,
                lineHeight = 18.sp,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Fecha nac: ${perfil.fechaNacimiento}",
                fontSize = 13.sp,
                color = TextoCardColor.copy(alpha = 0.9f)
            )

            Text(
                text = "Ciudad: ${perfil.ciudad}",
                fontSize = 13.sp,
                color = TextoCardColor.copy(alpha = 0.9f)
            )
        }
    }
}
package com.example.pantallas.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.AsyncImage // <--- Asegúrate de tener Coil importado
import com.example.pantallas.modelos.Perfil

// AHORA ACEPTAMOS 'foto' AQUÍ vvvvv
@Composable
fun CardPerfil(perfil: Perfil, foto: String? = null) {

    val FondoCardColor = Color(0xFF2E4053)
    val TextoCardColor = Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(FondoCardColor, shape = RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 1. Círculo de Foto de Perfil
        Box(
            modifier = Modifier
                .size(68.dp) // Tamaño ajustado para verse bien
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, TextoCardColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // LÓGICA: Si llega foto, la pintamos. Si no, texto.
            if (!foto.isNullOrEmpty()) {
                AsyncImage(
                    model = foto,
                    contentDescription = "Foto Perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "Foto",
                    color = Color.Black,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.size(12.dp))

        // 2. Columna de Datos
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${perfil.nombre} ${perfil.apellidos}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoCardColor,
                lineHeight = 16.sp,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Edad: ${perfil.fechaNacimiento}",
                fontSize = 12.sp,
                color = TextoCardColor
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Ciudad: ${perfil.ciudad}",
                fontSize = 12.sp,
                color = TextoCardColor
            )
        }
    }
}
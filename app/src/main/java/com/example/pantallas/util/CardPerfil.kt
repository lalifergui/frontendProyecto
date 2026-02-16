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
import java.time.LocalDate
import java.time.Period

/**
 * Componente reutilizable para mostrar la "Carta de Presentación".
 * Calcula la EDAD a partir de la fecha de nacimiento que guardamos en Editar Perfil.
 */
@Composable
fun CardPerfil(perfil: Perfil, foto: String? = null) {

    val FondoCardColor = MaterialTheme.colorScheme.primary
    val TextoCardColor = MaterialTheme.colorScheme.onPrimary

    // 🎯 CÁLCULO DE EDAD: De "1995-05-15" a "30 años"
    val edadCalculada = try {
        if (perfil.fechaNacimiento.isNotBlank()) {
            val fechaNac = LocalDate.parse(perfil.fechaNacimiento)
            val hoy = LocalDate.now()
            val anios = Period.between(fechaNac, hoy).years
            "$anios años"
        } else {
            "Edad no indicada"
        }
    } catch (e: Exception) {
        "Fecha no válida"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(FondoCardColor, shape = RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Círculo de Foto
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, TextoCardColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Usamos la foto que nos pasan (que ya trae la URL completa del PerfilViewModel)
            if (!foto.isNullOrEmpty()) {
                AsyncImage(
                    model = foto,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(40.dp))
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 2. Datos (Nombre y EDAD)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${perfil.nombre} ${perfil.apellidos}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextoCardColor
            )

            Spacer(modifier = Modifier.height(2.dp))

            //  AQUÍ SALE LA EDAD CALCULADA
            Text(
                text = edadCalculada,
                fontSize = 14.sp,
                color = TextoCardColor.copy(alpha = 0.9f)
            )

            Text(
                text = "Ciudad: ${perfil.ciudad}",
                fontSize = 13.sp,
                color = TextoCardColor.copy(alpha = 0.8f)
            )
        }
    }
}
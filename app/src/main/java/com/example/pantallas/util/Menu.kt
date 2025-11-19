package com.example.pantallas.util

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pantallas.ui.perfil.Perfil
import com.example.pantallas.ui.favoritos.Favoritos
import com.example.pantallas.ui.principal.Principal

@Composable
fun Menu(context: Context,modifier: Modifier = Modifier){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Casa",
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        val intent = Intent(context, Principal::class.java)
                        context.startActivity(intent)
                    }
            )
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = "Favoritos",
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        val intent = Intent(context, Favoritos::class.java)
                        context.startActivity(intent)
                    }
            )
            Icon(
                imageVector = Icons.Filled.MailOutline,
                contentDescription = "Chat",
                tint = Color.Black,
                modifier = Modifier.size(48.dp)
            )
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Perfil",
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        val intent = Intent(context, Perfil::class.java)
                        context.startActivity(intent)
                    }
            )
        }
    }
}
package com.example.pantallas.ui.login
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
class LoginViewModel : ViewModel(){
    val usuario = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun actualizarUsuario(nuevosUsuario: String){
        usuario.value=nuevosUsuario
    }
    fun actualizarPassword(nuevoPassword: String){
        password.value=nuevoPassword
    }

}
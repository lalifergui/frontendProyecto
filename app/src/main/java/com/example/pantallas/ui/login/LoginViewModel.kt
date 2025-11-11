package com.example.pantallas.ui.login
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantallas.data.dto.LoginDTO
import com.example.pantallas.data.dto.LoginResponse
import com.example.pantallas.data.repositorios.LoginRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel(

){
    private val _login=MutableStateFlow(LoginDTO())
    val login: StateFlow<LoginDTO> = _login
    private val _errorUsuario = MutableStateFlow(false)
    val errorUsuario: StateFlow<Boolean> = _errorUsuario
    private val _errorPassword = MutableStateFlow(false)
    val errorPassword: StateFlow<Boolean> = _errorPassword
    //necesitamos inyectar el LoginRepositorio
    private  val repository = LoginRepositorio()
    private  val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> get() = _loginResult

    fun actualizarLogin(nuevoLogin: LoginDTO){
        _login.value=nuevoLogin
    }
    val botonHabilitado= combine(
        _login,_errorUsuario,_errorPassword
    ){l->
        val dto = l[0] as LoginDTO
        val errEmail=l[1] as Boolean
        val errPassword=l[2] as Boolean
        val camposLlenos= dto.usuario.isNotBlank()==true&& dto.password.isNotBlank()==true
        val errores= !errPassword && !errEmail

        camposLlenos && errores
    }
    private fun validarEmail(input: String) {
        val regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        _errorUsuario.value = input.isNotEmpty() && !regexEmail.matches(input)
    }
    private fun validarPassword(input: String) {
        _errorPassword.value = input.isNotEmpty() && input.length < 4
    }
    init {
        // Observa los cambios de los inputs y actualiza los estados de error
        viewModelScope.launch {
            _login.collect { log ->
                validarEmail(input = log.usuario)
                validarPassword(input = log.password)
            }
        }
    }
    /**
     *  fun login(){
     *         viewModelScope.launch {
     *             try {
     *                 val response = repository.login(usuario.value, password.value)
     *                 _loginResult.value = response
     *             }catch (e: Exception){
     *                 e.printStackTrace()
     *                 _loginResult.value = null
     *             }
     *         }
     *     }
     */

}
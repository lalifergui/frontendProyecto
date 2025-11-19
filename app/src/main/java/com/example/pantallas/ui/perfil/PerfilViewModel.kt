package com.example.pantallas.ui.perfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pantallas.modelos.Perfil
import com.example.pantallas.modelos.Usuario // Asegúrate de importar Usuario

class PerfilViewModel : ViewModel() {

    // 1. ESTADO OBSERVABLE DEL PERFIL
    // Los Composables que lean esta variable se actualizarán automáticamente
    // cuando se modifique el valor (por ejemplo, después de una edición).
    var perfil: Perfil by mutableStateOf(Perfil.PerfilEjemplo)
        private set
    // 'private set' asegura que solo las funciones de este ViewModel pueden cambiar el estado.

    // 2. LÓGICA DE CARGA DE DATOS
    /**
     * Simula la obtención de los datos del perfil (por ejemplo, desde una API o DB).
     * @param perfilId El ID del perfil a cargar.
     */
    fun cargarPerfil(perfilId: Long) {
        // En una aplicación real (Producción):
        // 1. Usarías viewModelScope.launch { ... } (Coroutines)
        // 2. Llamarías a tu Repositorio para obtener los datos.
        // 3. Asignarías el resultado a la variable 'perfil'.

        // SIMULACIÓN actual: Cargar el perfil de ejemplo.
        if (perfilId == Perfil.PerfilEjemplo.perfil_id) {
            perfil = Perfil.PerfilEjemplo
        }
        // Podrías añadir lógica para cargar un perfil diferente o manejar un error.
    }

    // 3. LÓGICA DE ACTUALIZACIÓN DE DATOS
    /**
     * Se llama desde la pantalla de edición (EditarPerfil) para guardar los cambios.
     * @param nuevoNombre Nuevo valor del nombre.
     * @param nuevosApellidos Nuevo valor de los apellidos.
     * @param nuevaCiudad Nuevo valor de la ciudad.
     * // Puedes añadir más parámetros aquí
     */
    fun actualizarPerfil(
        nuevoNombre: String,
        nuevosApellidos: String,
        nuevaCiudad: String
    ) {
        // Creamos una copia del objeto 'Perfil' actual, modificando solo los campos necesarios.
        val perfilActualizado = perfil.copy(
            nombre = nuevoNombre,
            apellidos = nuevosApellidos,
            ciudad = nuevaCiudad
            // El resto de campos (fechaNacimiento, usuario) se mantienen iguales.
        )

        // Actualizamos el estado. Esto causa la Recomposición de la UI.
        perfil = perfilActualizado

        // En una aplicación real (Producción):
        // 1. Después de actualizar el estado local, deberías llamar a tu Repositorio
        //    para persistir estos cambios en la base de datos o el backend.
    }
}
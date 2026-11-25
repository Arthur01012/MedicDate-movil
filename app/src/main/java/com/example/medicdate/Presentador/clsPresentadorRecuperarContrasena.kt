package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsRecuperarModelo
import com.example.medicdate.Vista.RecuperarContrasenaActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorRecuperarContrasena(private val vista: RecuperarContrasenaActivity) {
    private val modelo = clsRecuperarModelo()

    fun cambiarContrasena(correo: String, respuesta: String, claveNueva: String, confirmar: String)
    {
        if (respuesta.isEmpty() || claveNueva.isEmpty() || confirmar.isEmpty())
        {
            return vista.mostrarError("Completa todos los campos")
        }
        if (claveNueva.length < 8)
        {
            return vista.mostrarError("La contraseña debe tener al menos 8 caracteres")
        }
        if (claveNueva != confirmar)
        {
            return vista.mostrarError("Las contraseñas no coinciden")
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val resultado = modelo.cambiarContrasena(correo, respuesta, claveNueva)
                if (resultado.exito)
                {
                    vista.contrasenaCambiada(resultado.mensaje)
                }
                else
                {
                    vista.mostrarError(resultado.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("No se pudo cambiar la contraseña: " + e.localizedMessage)
            }
        }
    }
}

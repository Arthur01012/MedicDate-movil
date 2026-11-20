package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsLoginModelo
import com.example.medicdate.Vista.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorLogin(private val vista: LoginActivity) {
    private val modelo = clsLoginModelo()

    fun realizarLogin(correo: String, clave: String)
    {
        if (correo.isEmpty() || clave.isEmpty())
        {
            return vista.mostrarError("Escribe tu correo y contraseña")
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.autenticarUsuario(correo, clave)
                if (respuesta.exito)
                {
                    vista.loginExitoso(respuesta.idUsuario, respuesta.idPaciente)
                }
                else
                {
                    vista.mostrarError(respuesta.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("Error al iniciar sesión: " + e.localizedMessage)
            }
        }
    }
}

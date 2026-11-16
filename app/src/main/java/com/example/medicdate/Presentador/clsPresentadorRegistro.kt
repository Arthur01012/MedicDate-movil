package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsRegistroModelo
import com.example.medicdate.Vista.RegistroActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorRegistro(private val vista: RegistroActivity) {
    private val modelo = clsRegistroModelo()

    fun crearCuenta(nombres: String, apellidoPaterno: String, apellidoMaterno: String, correo: String, telefono: String, clave: String)
    {
        // El apellido materno es opcional
        if (nombres.isEmpty() || apellidoPaterno.isEmpty() || correo.isEmpty() || telefono.isEmpty() || clave.isEmpty())
        {
            vista.mostrarError("Completa todos los campos obligatorios")
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.registrarUsuario(nombres, apellidoPaterno, apellidoMaterno, correo, telefono, clave)
                if (respuesta.exito)
                {
                    vista.registroExitoso(respuesta.mensaje, respuesta.idUsuario)
                }
                else
                {
                    vista.mostrarError(respuesta.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("No se pudo crear la cuenta: " + e.localizedMessage)
            }
        }
    }
}

package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsRecuperarModelo
import com.example.medicdate.Vista.RecuperarCorreoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorRecuperarCorreo(private val vista: RecuperarCorreoActivity) {
    private val modelo = clsRecuperarModelo()

    fun buscarPregunta(correo: String)
    {
        if (correo.isEmpty())
        {
            return vista.mostrarError("Escribe tu correo electrónico")
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val resultado = modelo.obtenerPregunta(correo)
                if (resultado.exito)
                {
                    vista.irAResponderPregunta(correo, resultado.pregunta)
                }
                else
                {
                    vista.mostrarError(resultado.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("No se pudo buscar la cuenta: " + e.localizedMessage)
            }
        }
    }
}

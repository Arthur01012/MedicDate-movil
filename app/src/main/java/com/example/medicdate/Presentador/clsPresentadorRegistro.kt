package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsRegistroModelo
import com.example.medicdate.Modelo.dataPregunta
import com.example.medicdate.Vista.RegistroActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorRegistro(private val vista: RegistroActivity) {
    private val modelo = clsRegistroModelo()
    private var listaPreguntas: List<dataPregunta> = emptyList()

    fun cargarPreguntas()
    {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerPreguntas()
                if (respuesta.exito)
                {
                    listaPreguntas = respuesta.preguntas
                    vista.mostrarPreguntas(listaPreguntas)
                }
                else
                {
                    vista.mostrarError(respuesta.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("No se pudieron cargar las preguntas: " + e.localizedMessage)
            }
        }
    }

    fun crearCuenta(nombres: String, apellidoPaterno: String, apellidoMaterno: String, correo: String, telefono: String, clave: String, posicion: Int, respuesta: String)
    {
        // El apellido materno es opcional
        if (nombres.isEmpty() || apellidoPaterno.isEmpty() || correo.isEmpty() || telefono.isEmpty() || clave.isEmpty())
        {
            return vista.mostrarError("Completa todos los campos obligatorios")
        }
        if (listaPreguntas.isEmpty() || respuesta.isEmpty())
        {
            return vista.mostrarError("Elige tu pregunta secreta y escribe tu respuesta")
        }

        val idPregunta = listaPreguntas[posicion].idPregunta

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val resultado = modelo.registrarUsuario(nombres, apellidoPaterno, apellidoMaterno, correo, telefono, clave, idPregunta, respuesta)
                if (resultado.exito)
                {
                    vista.registroExitoso(resultado.mensaje, resultado.idUsuario, resultado.idPaciente)
                }
                else
                {
                    vista.mostrarError(resultado.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("No se pudo crear la cuenta: " + e.localizedMessage)
            }
        }
    }
}
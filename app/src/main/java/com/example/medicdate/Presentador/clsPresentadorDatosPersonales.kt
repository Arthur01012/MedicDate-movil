package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsDatosPersonalesModelo
import com.example.medicdate.Modelo.DatosPersonalesRequest
import com.example.medicdate.Vista.DatosPersonalesActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorDatosPersonales(private val vista: DatosPersonalesActivity) {
    private val modelo = clsDatosPersonalesModelo()

    fun cargarDatos(idUsuario: Int)
    {
        // Si no llego el id, se regresa al login
        if (idUsuario == 0)
        {
            return vista.irALogin()
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val datos = modelo.obtenerDatosPersonales(idUsuario)
                if (datos.exito)
                {
                    vista.mostrarDatos(datos.nombres, datos.apellidoPaterno, datos.apellidoMaterno, datos.telefono, datos.correo)
                }
                else
                {
                    vista.mostrarError(datos.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("Error al cargar los datos: " + e.localizedMessage)
            }
        }
    }

    fun guardarDatos(idUsuario: Int, nombres: String, paterno: String, materno: String, telefono: String)
    {
        if (nombres.isEmpty() || paterno.isEmpty() || telefono.isEmpty())
        {
            return vista.mostrarError("Completa nombres, apellido paterno y teléfono")
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val request = DatosPersonalesRequest(idUsuario, nombres, paterno, materno, telefono)
                val respuesta = modelo.guardarDatosPersonales(request)
                if (respuesta.exito)
                {
                    vista.datosGuardados(respuesta.mensaje)
                }
                else
                {
                    vista.mostrarError(respuesta.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("Error al guardar los datos: " + e.localizedMessage)
            }
        }
    }

}
package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsDisponibilidadModelo
import com.example.medicdate.Modelo.DisponibilidadRequest
import com.example.medicdate.Vista.RecervarCitaActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorReservaCita(private val vista: RecervarCitaActivity) {
    private val modelo = clsDisponibilidadModelo()

    fun cargarDisponibilidad(idEmpleado: Int?, idTipoAtencion: Int, fecha: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val request = DisponibilidadRequest(id_empleado = idEmpleado, id_tipo_atencion = idTipoAtencion, fecha = fecha)
                val respuesta = modelo.obtenerDisponibilidad(request)
                if (respuesta.exito) {
                    vista.mostrarHorarios(respuesta.horarios)
                } else {
                    vista.mostrarError(respuesta.mensaje ?: "Error desconocido al cargar disponibilidad")
                }
            } catch (e: Exception) {
                vista.mostrarError("Error al cargar disponibilidad: ${e.localizedMessage}")
            }
        }
    }
}

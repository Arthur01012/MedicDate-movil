package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsReservaCitaModelo
import com.example.medicdate.Vista.RecervarCitaActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorReservaCita(private val vista: RecervarCitaActivity) {
    private val modelo = clsReservaCitaModelo()

    fun cargarDisponibilidad(idEmpleado: Int?, idTipoAtencion: Int, fecha: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Build request for Disponibilidad.php (date + doctor)
                val resp = modelo.obtenerDisponibilidad(/* build map */)
                if (resp.exito) {
                    vista.mostrarHorarios(resp.horarios)
                } else {
                    vista.mostrarError(resp.mensaje)
                }
            } catch (e: Exception) {
                vista.mostrarError("Error al cargar disponibilidad: ${e.localizedMessage}")
            }
        }
    }

    fun reservarCita(request: ReservaRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val resp = modelo.reservarCita(request)
                if (resp.exito && resp.id_cita != null) {
                    vista.irACitaConfirmada(resp.id_cita)
                } else {
                    vista.mostrarError(resp.mensaje)
                }
            } catch (e: Exception) {
                vista.mostrarError("Error al reservar: ${e.localizedMessage}")
            }
        }
    }
}
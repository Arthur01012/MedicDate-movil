package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.ReservaRequest
import com.example.medicdate.Modelo.clsReservaCitaModelo
import com.example.medicdate.Vista.ConfirmarCitaActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorConfirmarCita(private val vista: ConfirmarCitaActivity) {
    private val modelo = clsReservaCitaModelo()

    fun reservarCita(request: ReservaRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.reservarCita(request)
                if (respuesta.exito && respuesta.id_cita != null) {
                    vista.irAConfirmada(respuesta.id_cita)
                } else {
                    vista.mostrarError(respuesta.mensaje)
                }
            } catch (e: Exception) {
                vista.mostrarError("Error al reservar cita: ${e.localizedMessage}")
            }
        }
    }
}

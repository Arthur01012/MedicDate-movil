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
                val idCitaFinal = respuesta.id_cita ?: respuesta.cita?.id_cita
                if (respuesta.exito && idCitaFinal != null && idCitaFinal > 0) {
                    vista.irAConfirmada(idCitaFinal)
                } else if (respuesta.exito) {
                    vista.irAConfirmada(1)
                } else {
                    vista.mostrarError(if (respuesta.mensaje.isNotEmpty()) respuesta.mensaje else "Error al procesar la cita")
                }
            } catch (e: Exception) {
                vista.mostrarError("Error al reservar cita: ${e.localizedMessage}")
            }
        }
    }
}

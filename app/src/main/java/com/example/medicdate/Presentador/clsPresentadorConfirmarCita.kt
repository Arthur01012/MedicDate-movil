package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.ReservaRequest
import com.example.medicdate.Modelo.ReservaResponse
import com.example.medicdate.Modelo.clsReservaCitaModelo
import com.example.medicdate.Vista.ConfirmarCitaActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsPresentadorConfirmarCita(private val vista: ConfirmarCitaActivity) {
    private val modelo = clsReservaCitaModelo()
    private var llamadaActual: Call<ReservaResponse>? = null

    fun reservarCita(request: ReservaRequest) {
        llamadaActual?.cancel()
        vista.mostrarCargando(true)

        val llamada = modelo.reservarCita(request)
        llamadaActual = llamada
        llamada.enqueue(object : Callback<ReservaResponse> {
            override fun onResponse(
                call: Call<ReservaResponse>,
                response: Response<ReservaResponse>
            ) {
                if (llamadaActual !== call || call.isCanceled) {
                    return
                }
                llamadaActual = null
                vista.mostrarCargando(false)

                val cuerpo = if (response.isSuccessful) {
                    response.body()
                } else {
                    modelo.parsearError(response.errorBody()?.string())
                }

                if (cuerpo != null && cuerpo.exito) {
                    val idCitaFinal = cuerpo.id_cita ?: cuerpo.cita?.id_cita
                    val consultorioConfirmado = cuerpo.cita?.consultorio
                    if (idCitaFinal != null && idCitaFinal > 0) {
                        vista.irAConfirmada(idCitaFinal, consultorioConfirmado)
                    } else {
                        vista.irAConfirmada(1, consultorioConfirmado)
                    }
                    return
                }

                val codigo = cuerpo?.codigo.orEmpty()
                val accion = cuerpo?.accion.orEmpty()
                val mensaje = if (cuerpo != null && cuerpo.mensaje.isNotBlank()) {
                    cuerpo.mensaje
                } else {
                    "Error al procesar la cita (HTTP ${response.code()})."
                }

                if (esConflictoDeHorario(response.code(), codigo, accion)) {
                    vista.mostrarConflictoHorario(mensaje)
                } else {
                    vista.mostrarError(mensaje)
                }
            }

            override fun onFailure(call: Call<ReservaResponse>, t: Throwable) {
                if (llamadaActual !== call || call.isCanceled) {
                    return
                }
                llamadaActual = null
                vista.mostrarCargando(false)
                vista.mostrarError("Error al reservar cita: ${t.localizedMessage}")
            }
        })
    }

    private fun esConflictoDeHorario(httpCode: Int, codigo: String, accion: String): Boolean {
        if (httpCode == 409 || accion == "elegir_otro_horario") {
            return true
        }
        return codigo == "MEDICO_OCUPADO" ||
            codigo == "PACIENTE_OCUPADO" ||
            codigo == "CONSULTORIO_OCUPADO" ||
            codigo == "AGENDA_BLOQUEADA" ||
            codigo == "HORARIO_NO_DISPONIBLE" ||
            codigo == "CONSULTORIO_NO_DISPONIBLE" ||
            codigo == "AGENDA_ACTUALIZADA"
    }

    fun cancelarSolicitudes() {
        llamadaActual?.cancel()
        llamadaActual = null
    }
}

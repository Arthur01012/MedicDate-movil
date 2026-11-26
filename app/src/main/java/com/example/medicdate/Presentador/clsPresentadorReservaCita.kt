package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.DisponibilidadRequest
import com.example.medicdate.Modelo.DisponibilidadResponse
import com.example.medicdate.Modelo.clsDisponibilidadModelo
import com.example.medicdate.Vista.RecervarCitaActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsPresentadorReservaCita(private val vista: RecervarCitaActivity) {
    private val modelo = clsDisponibilidadModelo()
    private var llamadaActual: Call<DisponibilidadResponse>? = null

    fun cargarDisponibilidad(
        idEmpleado: Int?,
        idTipoAtencion: Int,
        fecha: String,
        idUsuario: Int? = null
    ) {
        llamadaActual?.cancel()

        val request = DisponibilidadRequest(
            id_empleado = idEmpleado,
            id_tipo_atencion = idTipoAtencion,
            fecha = fecha,
            id_usuario = idUsuario
        )

        val llamadaGet = modelo.obtenerDisponibilidadGet(request)
        llamadaActual = llamadaGet
        llamadaGet.enqueue(object : Callback<DisponibilidadResponse> {
            override fun onResponse(
                call: Call<DisponibilidadResponse>,
                response: Response<DisponibilidadResponse>
            ) {
                if (llamadaActual !== call || call.isCanceled) {
                    return
                }

                if (response.isSuccessful) {
                    val cuerpo = response.body()
                    if (cuerpo != null && (cuerpo.exito || !cuerpo.mensaje.isNullOrEmpty())) {
                        llamadaActual = null
                        procesarRespuesta(cuerpo)
                        return
                    }
                } else {
                    val errorResp = modelo.parsearError(response.errorBody()?.string())
                    if (errorResp != null) {
                        llamadaActual = null
                        procesarRespuesta(errorResp)
                        return
                    }
                }

                consultarConFallbackPost(request)
            }

            override fun onFailure(call: Call<DisponibilidadResponse>, t: Throwable) {
                if (llamadaActual !== call || call.isCanceled) {
                    return
                }
                consultarConFallbackPost(request)
            }
        })
    }

    private fun consultarConFallbackPost(request: DisponibilidadRequest) {
        val llamadaPost = modelo.obtenerDisponibilidadPost(request)
        llamadaActual = llamadaPost
        llamadaPost.enqueue(object : Callback<DisponibilidadResponse> {
            override fun onResponse(
                call: Call<DisponibilidadResponse>,
                response: Response<DisponibilidadResponse>
            ) {
                if (llamadaActual !== call || call.isCanceled) {
                    return
                }
                llamadaActual = null

                if (response.isSuccessful) {
                    val cuerpo = response.body()
                    if (cuerpo != null) {
                        procesarRespuesta(cuerpo)
                        return
                    }
                    vista.mostrarError("El servidor no devolvió disponibilidad.")
                    return
                }

                val errorResp = modelo.parsearError(response.errorBody()?.string())
                if (errorResp != null) {
                    procesarRespuesta(errorResp)
                    return
                }
                vista.mostrarError("Error al cargar disponibilidad (HTTP ${response.code()}).")
            }

            override fun onFailure(call: Call<DisponibilidadResponse>, t: Throwable) {
                if (llamadaActual !== call || call.isCanceled) {
                    return
                }
                llamadaActual = null
                vista.mostrarError("Error al cargar disponibilidad: ${t.localizedMessage}")
            }
        })
    }

    private fun procesarRespuesta(respuesta: DisponibilidadResponse) {
        if (respuesta.exito) {
            vista.mostrarHorarios(
                respuesta.horarios,
                respuesta.dias_atencion,
                respuesta.mensaje
            )
        } else {
            vista.mostrarError(
                respuesta.mensaje ?: "Error al cargar disponibilidad",
                respuesta.dias_atencion
            )
        }
    }

    fun cancelarSolicitudes() {
        llamadaActual?.cancel()
        llamadaActual = null
    }
}

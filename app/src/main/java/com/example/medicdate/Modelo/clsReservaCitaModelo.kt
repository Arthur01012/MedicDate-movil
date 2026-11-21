package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente

data class ReservaRequest(
    val id_usuario: Int,
    val id_empleado: Int?,
    val id_tipo_atencion: Int,
    val inicio: String, // ISO‑8601 datetime
    val id_consultorio: Int? = null
)

data class ReservaResponse(
    val exito: Boolean,
    val codigo: String,
    val mensaje: String,
    val id_cita: Int?
)

class clsReservaCitaModelo {
    private val api: ApiService = RetrofitCliente.apiService

    suspend fun reservarCita(req: ReservaRequest): ReservaResponse {
        return api.reservarCita(req)
    }
}
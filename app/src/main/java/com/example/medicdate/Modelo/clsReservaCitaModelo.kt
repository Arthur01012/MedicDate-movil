package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReservaRequest(
    @SerialName("id_usuario") val id_usuario: Int,
    @SerialName("id_empleado") val id_empleado: Int?,
    @SerialName("id_tipo_atencion") val id_tipo_atencion: Int,
    @SerialName("inicio") val inicio: String, // ISO-8601 datetime
    @SerialName("id_consultorio") val id_consultorio: Int? = null
)

@Serializable
data class ReservaResponse(
    @SerialName("exito") val exito: Boolean = false,
    @SerialName("codigo") val codigo: String = "",
    @SerialName("mensaje") val mensaje: String = "",
    @SerialName("id_cita") val id_cita: Int? = null
)

class clsReservaCitaModelo {
    private val api: ApiService = RetrofitCliente.apiService

    suspend fun reservarCita(req: ReservaRequest): ReservaResponse {
        return api.reservarCita(req)
    }
}

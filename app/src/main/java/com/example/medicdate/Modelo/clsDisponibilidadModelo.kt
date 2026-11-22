package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// Request for availability (date, doctor, specialty)
@Serializable
data class DisponibilidadRequest(
    @SerialName("id_empleado") val id_empleado: Int?,
    @SerialName("id_tipo_atencion") val id_tipo_atencion: Int,
    @SerialName("fecha") val fecha: String // format YYYY-MM-DD
)

// Response containing a list of time slots (horario) and doctor info
@Serializable
data class DisponibilidadResponse(
    @SerialName("exito") val exito: Boolean = false,
    @SerialName("mensaje") val mensaje: String? = null,
    @SerialName("medico") val medico: String? = null,
    @SerialName("tipo_atencion") val tipo_atencion: String? = null,
    @SerialName("especialidad") val especialidad: String? = null,
    @SerialName("duracion_minutos") val duracion_minutos: Int = 0,
    @SerialName("horarios") val horarios: List<Horario> = emptyList()
)

class clsDisponibilidadModelo {
    private val api: ApiService = RetrofitCliente.apiService

    suspend fun obtenerDisponibilidad(request: DisponibilidadRequest): DisponibilidadResponse =
        withContext(Dispatchers.IO) {
            try {
                // Try POST first
                val resp = api.obtenerDisponibilidad(request)
                if (resp.exito || !resp.mensaje.isNullOrEmpty()) {
                    return@withContext resp
                }
            } catch (_: Exception) {
            }

            // Fallback to GET with JSON parameter "datos"
            val datosJson = Json.encodeToString(DisponibilidadRequest.serializer(), request)
            api.obtenerDisponibilidadGet(datosJson)
        }
}

package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException

@Serializable
data class CitaDetalleReserva(
    @SerialName("id_cita") val id_cita: Int = 0,
    @SerialName("estado") val estado: String = "",
    @SerialName("fecha") val fecha: String = "",
    @SerialName("hora") val hora: String = "",
    @SerialName("duracion_minutos") val duracion_minutos: Int = 0,
    @SerialName("medico") val medico: String? = null,
    @SerialName("tipo_atencion") val tipo_atencion: String? = null,
    @SerialName("especialidad") val especialidad: String? = null,
    @SerialName("consultorio") val consultorio: String? = null
)

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
    @SerialName("id_cita") val id_cita: Int? = null,
    @SerialName("cita") val cita: CitaDetalleReserva? = null
)

class clsReservaCitaModelo {
    private val api: ApiService = RetrofitCliente.apiService
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    suspend fun reservarCita(req: ReservaRequest): ReservaResponse =
        withContext(Dispatchers.IO) {
            try {
                api.reservarCita(req)
            } catch (e: HttpException) {
                try {
                    val errorBody = e.response()?.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        return@withContext jsonConfig.decodeFromString<ReservaResponse>(errorBody)
                    }
                } catch (_: Exception) {
                }
                ReservaResponse(exito = false, mensaje = e.message())
            } catch (e: Exception) {
                ReservaResponse(exito = false, mensaje = e.localizedMessage ?: "Error al reservar cita")
            }
        }
}

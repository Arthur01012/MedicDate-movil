package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException

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
    @SerialName("codigo") val codigo: String? = null,
    @SerialName("medico") val medico: String? = null,
    @SerialName("tipo_atencion") val tipo_atencion: String? = null,
    @SerialName("especialidad") val especialidad: String? = null,
    @SerialName("duracion_minutos") val duracion_minutos: Int = 0,
    @SerialName("horarios") val horarios: List<Horario> = emptyList()
)

class clsDisponibilidadModelo {
    private val api: ApiService = RetrofitCliente.apiService
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    suspend fun obtenerDisponibilidad(request: DisponibilidadRequest): DisponibilidadResponse =
        withContext(Dispatchers.IO) {
            // First try GET with JSON parameter "datos" because disponibilidad.php uses citasApiLeerDatosQuery()
            try {
                val datosJson = jsonConfig.encodeToString(DisponibilidadRequest.serializer(), request)
                val resp = api.obtenerDisponibilidadGet(datosJson)
                if (resp.exito || !resp.mensaje.isNullOrEmpty()) {
                    return@withContext resp
                }
            } catch (e: HttpException) {
                val errorResp = parseHttpError(e)
                if (errorResp != null) {
                    return@withContext errorResp
                }
            } catch (_: Exception) {
            }

            // Fallback: try POST
            try {
                return@withContext api.obtenerDisponibilidad(request)
            } catch (e: HttpException) {
                val errorResp = parseHttpError(e)
                if (errorResp != null) {
                    return@withContext errorResp
                }
                DisponibilidadResponse(exito = false, mensaje = e.message())
            } catch (e: Exception) {
                DisponibilidadResponse(exito = false, mensaje = e.localizedMessage)
            }
        }

    private fun parseHttpError(e: HttpException): DisponibilidadResponse? {
        return try {
            val errorBody = e.response()?.errorBody()?.string() ?: return null
            jsonConfig.decodeFromString<DisponibilidadResponse>(errorBody)
        } catch (_: Exception) {
            null
        }
    }
}

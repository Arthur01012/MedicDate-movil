package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.InterfaceApi.RetrofitCliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Request for availability (date, doctor, specialty)
data class DisponibilidadRequest(
    val id_empleado: Int?,
    val id_tipo_atencion: Int,
    val fecha: String // format YYYY-MM-DD
)

// Simplified response containing a list of time slots (horario)
data class DisponibilidadResponse(
    val exito: Boolean,
    val mensaje: String?,
    val horarios: List<Horario>
)

// Represents a single time slot
data class Horario(
    val id_horario: Int,
    val hora_inicio: String, // HH:mm
    val hora_fin: String,
    val id_consultorio: Int?
)

class clsDisponibilidadModelo {
    private val api: ApiService = RetrofitCliente.apiService

    suspend fun obtenerDisponibilidad(request: DisponibilidadRequest): DisponibilidadResponse =
        withContext(Dispatchers.IO) {
            api.obtenerDisponibilidad(request)
        }
}

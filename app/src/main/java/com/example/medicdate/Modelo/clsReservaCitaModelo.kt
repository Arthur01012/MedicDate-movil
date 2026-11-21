package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.InterfaceApi.RetrofitCliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Data classes matching the PHP API request/response

data class ReservaRequest(
    val id_usuario: Int,
    val id_empleado: Int?,
    val id_tipo_atencion: Int,
    val inicio: String, // ISO‑8601 datetime string
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

    suspend fun reservarCita(request: ReservaRequest): ReservaResponse = withContext(Dispatchers.IO) {
        api.reservarCita(request)
    }

    // Optional: method to fetch availability (depends on your API design)
    // suspend fun obtenerDisponibilidad(...): DisponibilidadResponse { ... }
}

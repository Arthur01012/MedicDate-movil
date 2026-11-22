package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import retrofit2.HttpException

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

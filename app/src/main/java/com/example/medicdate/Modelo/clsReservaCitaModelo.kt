package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import kotlinx.serialization.json.Json
import retrofit2.Call

class clsReservaCitaModelo {
    private val api: ApiService = RetrofitCliente.apiService
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    fun reservarCita(req: ReservaRequest): Call<ReservaResponse> {
        return api.reservarCitaCall(req)
    }

    fun parsearError(errorBody: String?): ReservaResponse? {
        if (errorBody.isNullOrEmpty()) {
            return null
        }
        return try {
            jsonConfig.decodeFromString(
                ReservaResponse.serializer(),
                errorBody
            )
        } catch (_: Exception) {
            null
        }
    }
}

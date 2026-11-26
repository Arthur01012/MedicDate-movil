package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import retrofit2.Call

class clsReservaCitaModelo {
    private val api: ApiService = RetrofitCliente.apiService

    fun reservarCita(req: ReservaRequest): Call<ReservaResponse> {
        return api.reservarCitaCall(req)
    }

    fun parsearError(errorBody: String?): ReservaResponse? {
        if (errorBody.isNullOrEmpty()) {
            return null
        }
        return try {
            RetrofitCliente.jsonConfig.decodeFromString(
                ReservaResponse.serializer(),
                errorBody
            )
        } catch (_: Exception) {
            null
        }
    }
}

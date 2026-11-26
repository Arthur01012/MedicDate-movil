package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import kotlinx.serialization.json.Json
import retrofit2.Call

class clsDisponibilidadModelo {
    private val api: ApiService = RetrofitCliente.apiService
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    fun obtenerDisponibilidadGet(request: DisponibilidadRequest): Call<DisponibilidadResponse> {
        val datosJson = jsonConfig.encodeToString(
            DisponibilidadRequest.serializer(),
            request
        )
        return api.obtenerDisponibilidadGetCall(datosJson)
    }

    fun obtenerDisponibilidadPost(request: DisponibilidadRequest): Call<DisponibilidadResponse> {
        return api.obtenerDisponibilidadCall(request)
    }

    fun parsearError(errorBody: String?): DisponibilidadResponse? {
        if (errorBody.isNullOrEmpty()) {
            return null
        }
        return try {
            jsonConfig.decodeFromString(
                DisponibilidadResponse.serializer(),
                errorBody
            )
        } catch (_: Exception) {
            null
        }
    }
}

package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import retrofit2.Call

class clsDisponibilidadModelo {
    private val api: ApiService = RetrofitCliente.apiService

    fun obtenerDisponibilidadGet(request: DisponibilidadRequest): Call<DisponibilidadResponse> {
        val datosJson = RetrofitCliente.jsonConfig.encodeToString(
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
            RetrofitCliente.jsonConfig.decodeFromString(
                DisponibilidadResponse.serializer(),
                errorBody
            )
        } catch (_: Exception) {
            null
        }
    }
}

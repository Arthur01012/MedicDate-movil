package com.example.medicdate.Modelo

import com.example.medicdate.InterfaceApi.ApiService
import com.example.medicdate.Red.RetrofitCliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import retrofit2.HttpException

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

package com.example.medicdate.Modelo

import com.example.medicdate.Red.RetrofitCliente

class clsRecuperarModelo {
    suspend fun obtenerPregunta(correo: String): PreguntaRecuperacionResponse {
        val request = CorreoRecuperarRequest(correo = correo)
        return RetrofitCliente.apiService.obtenerPreguntaRecuperacion(request)
    }

    suspend fun cambiarContrasena(correo: String, respuesta: String, claveNueva: String): RecuperarResponse {
        val request = RecuperarRequest(correo = correo, respuesta = respuesta, claveNueva = claveNueva)
        return RetrofitCliente.apiService.recuperarContrasena(request)
    }
}

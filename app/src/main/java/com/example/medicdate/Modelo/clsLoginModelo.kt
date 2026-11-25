package com.example.medicdate.Modelo

import com.example.medicdate.Red.RetrofitCliente

class clsLoginModelo {
    suspend fun autenticarUsuario(correo: String, clave: String): LoginResponse {
        val request = LoginRequest(correo = correo, clave = clave)
        return RetrofitCliente.apiService.iniciarSesion(request)
    }
}

package com.example.medicdate.Modelo

class clsLoginModelo {
    suspend fun autenticarUsuario(correo: String, clave: String): dataLogin {
        val request = LoginRequest(correo = correo, clave = clave)
        return RetrofitCliente.apiService.iniciarSesion(request)
    }
}

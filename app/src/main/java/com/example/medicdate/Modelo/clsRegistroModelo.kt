package com.example.medicdate.Modelo

class clsRegistroModelo {
    suspend fun registrarUsuario(nombres: String, apellidoPaterno: String, apellidoMaterno: String, correo: String, telefono: String, clave: String): dataRegistro {
        return RetrofitCliente.apiService.registrarUsuario(nombres, apellidoPaterno, apellidoMaterno, correo, telefono, clave)
    }
}

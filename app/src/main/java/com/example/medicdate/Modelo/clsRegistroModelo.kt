package com.example.medicdate.Modelo

import com.example.medicdate.Red.RetrofitCliente

class clsRegistroModelo {
    suspend fun registrarUsuario(nombres: String, apellidoPaterno: String, apellidoMaterno: String, correo: String, telefono: String, clave: String): dataRegistro {
        return RetrofitCliente.apiService.registrarUsuario(nombres, apellidoPaterno, apellidoMaterno, correo, telefono, clave)
    }
}

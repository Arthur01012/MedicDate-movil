package com.example.medicdate.Modelo

import com.example.medicdate.Red.RetrofitCliente

class clsDatosPersonalesModelo {
    suspend fun obtenerDatosPersonales(idUsuario: Int): DatosPersonalesResponse {
        return RetrofitCliente.apiService.obtenerDatosPersonales(idUsuario, System.currentTimeMillis())
    }
    suspend fun guardarDatosPersonales(request: DatosPersonalesRequest): GuardarDatosPersonalesResponse {
        return RetrofitCliente.apiService.actualizarDatosPersonales(request)
    }
}
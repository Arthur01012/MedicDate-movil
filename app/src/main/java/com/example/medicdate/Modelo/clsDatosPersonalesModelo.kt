package com.example.medicdate.Modelo

import com.example.medicdate.Red.RetrofitCliente

class clsDatosPersonalesModelo {
    suspend fun obtenerDatosPersonales(idUsuario: Int): dataDatosPersonales {
        return RetrofitCliente.apiService.obtenerDatosPersonales(idUsuario, System.currentTimeMillis())
    }
    suspend fun guardarDatosPersonales(request: DatosPersonalesRequest): dataGuardarDatosPersonales {
        return RetrofitCliente.apiService.actualizarDatosPersonales(request)
    }
}
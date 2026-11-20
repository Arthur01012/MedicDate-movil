package com.example.medicdate.Modelo

import com.example.medicdate.Red.RetrofitCliente

class clsCuentaModelo {
    suspend fun obtenerCuenta(idUsuario: Int): dataCuenta {
        return RetrofitCliente.apiService.obtenerCuenta(idUsuario)
    }
}

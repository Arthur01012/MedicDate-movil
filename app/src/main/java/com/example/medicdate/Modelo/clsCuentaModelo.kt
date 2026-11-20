package com.example.medicdate.Modelo

class clsCuentaModelo {
    suspend fun obtenerCuenta(idUsuario: Int): dataCuenta {
        return RetrofitCliente.apiService.obtenerCuenta(idUsuario)
    }
}

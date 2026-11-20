package com.example.medicdate.Modelo

class clsExplorarModelo {

    suspend fun obtenerInformacion(
        idPaciente: Int
    ): dataExplorar {

        return RetrofitCliente.apiService.obtenerExplorar(
            idPaciente
        )
    }
}
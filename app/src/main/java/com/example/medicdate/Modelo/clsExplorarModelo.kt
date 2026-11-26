package com.example.medicdate.Modelo
import com.example.medicdate.Red.RetrofitCliente
class clsExplorarModelo {

    suspend fun obtenerInformacion(
        idPaciente: Int
    ): dataExplorar {

        return RetrofitCliente.apiService.obtenerExplorar(
            idPaciente
        )
    }
}
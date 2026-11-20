package com.example.medicdate.Modelo

import com.example.medicdate.Red.RetrofitCliente

class clsMisCitasModelo {
    suspend fun obtenerCitas(idUsuario: Int): List<dataCitaPaciente> {
        return RetrofitCliente.apiService.obtenerCitasPaciente(idUsuario)
    }

    suspend fun obtenerDetalle(idCita: Int, idUsuario: Int): dataDetalleCita {
        return RetrofitCliente.apiService.obtenerDetalleCita(idCita, idUsuario)
    }
}

package com.example.medicdate.Modelo
import com.example.medicdate.Red.RetrofitCliente

class clsMedicosEspecialidadModelo {

    suspend fun obtenerMedicos(
        idEspecialidad: Int
    ): dataMedicosEspecialidad {

        return RetrofitCliente.apiService.obtenerMedicosEspecialidad(
            idEspecialidad
        )
    }
}
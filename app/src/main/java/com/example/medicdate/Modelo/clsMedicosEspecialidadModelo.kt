package com.example.medicdate.Modelo

class clsMedicosEspecialidadModelo {

    suspend fun obtenerMedicos(
        idEspecialidad: Int
    ): dataMedicosEspecialidad {

        return RetrofitCliente.apiService.obtenerMedicosEspecialidad(
            idEspecialidad
        )
    }
}
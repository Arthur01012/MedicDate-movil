package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsMedicosEspecialidadModelo
import com.example.medicdate.Vista.MedicosEspecialidadActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorMedicosEspecialidad(private val vista: MedicosEspecialidadActivity) {
    private val modelo = clsMedicosEspecialidadModelo()

    fun cargarMedicos(idEspecialidad: Int, nombreEspecialidadInicial: String = "") {
        if (nombreEspecialidadInicial.isNotBlank()) {
            vista.mostrarTituloEspecialidad(nombreEspecialidadInicial)
        }

        if (idEspecialidad <= 0) {
            vista.mostrarError("Id de especialidad no válido.")
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerMedicos(idEspecialidad)
                if (respuesta.success) {
                    val nombreFinal = respuesta.especialidad?.nombre ?: nombreEspecialidadInicial
                    if (nombreFinal.isNotBlank()) {
                        vista.mostrarTituloEspecialidad(nombreFinal)
                    }
                    vista.mostrarMedicos(respuesta.medicos, nombreFinal)
                } else {
                    vista.mostrarError(
                        respuesta.mensaje.ifEmpty { "No se encontraron médicos para esta especialidad." }
                    )
                }
            } catch (e: Exception) {
                vista.mostrarError(
                    "Error al consultar médicos: " + (e.localizedMessage ?: "Error desconocido")
                )
            }
        }
    }
}
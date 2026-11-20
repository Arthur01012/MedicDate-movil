package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.Especialidad
import com.example.medicdate.Modelo.MedicoExplorar
import com.example.medicdate.Modelo.clsExplorarModelo
import com.example.medicdate.Vista.ExplorarActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorExplorar(private val vista: ExplorarActivity) {
    private val modelo = clsExplorarModelo()

    private var especialidadesCompletas: List<Especialidad> = emptyList()
    private var medicosCompletos: List<MedicoExplorar> = emptyList()

    fun cargarExplorar(idPaciente: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerInformacion(idPaciente)
                if (respuesta.success) {
                    val nombreCompleto = respuesta.paciente?.nombre?.trim().orEmpty()
                    val primerNombre = nombreCompleto.split("\\s+".toRegex()).firstOrNull().orEmpty()
                    vista.mostrarSaludo(
                        if (primerNombre.isNotEmpty()) "Hola, $primerNombre" else "Hola"
                    )

                    vista.mostrarProximaCita(respuesta.proximaCita)

                    especialidadesCompletas = respuesta.especialidades
                    medicosCompletos = respuesta.medicos

                    vista.mostrarEspecialidades(especialidadesCompletas)
                    vista.mostrarMedicos(medicosCompletos)
                } else {
                    vista.mostrarError(
                        respuesta.mensaje.ifEmpty { "No se pudo cargar la información de explorar." }
                    )
                }
            } catch (e: Exception) {
                vista.mostrarError(
                    "Error al cargar explorar: " + (e.localizedMessage ?: "Error desconocido")
                )
            }
        }
    }

    fun filtrarBusqueda(texto: String) {
        val termino = texto.trim().lowercase()
        if (termino.isEmpty()) {
            vista.mostrarEspecialidades(especialidadesCompletas)
            vista.mostrarMedicos(medicosCompletos)
            return
        }

        val especialidadesFiltradas = especialidadesCompletas.filter { esp ->
            esp.nombre.lowercase().contains(termino) ||
                    esp.descripcion.lowercase().contains(termino)
        }

        val medicosFiltrados = medicosCompletos.filter { med ->
            med.nombre.lowercase().contains(termino) ||
                    med.especialidades.lowercase().contains(termino)
        }

        vista.mostrarEspecialidades(especialidadesFiltradas)
        vista.mostrarMedicos(medicosFiltrados)
    }
}
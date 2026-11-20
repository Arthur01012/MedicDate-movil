package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataExplorar(

    @SerialName("success")
    val success: Boolean = false,

    @SerialName("paciente")
    val paciente: Paciente? = null,

    @SerialName("proxima_cita")
    val proximaCita: ProximaCita? = null,

    @SerialName("especialidades")
    val especialidades: List<Especialidad> = emptyList(),

    @SerialName("medicos")
    val medicos: List<MedicoExplorar> = emptyList(),

    @SerialName("mensaje")
    val mensaje: String = ""
)

@Serializable
data class Paciente(

    @SerialName("id_paciente")
    val idPaciente: Int = 0,

    @SerialName("nombre")
    val nombre: String = ""
)

@Serializable
data class ProximaCita(

    @SerialName("id_cita")
    val idCita: Int = 0,

    @SerialName("inicio")
    val inicio: String = "",

    @SerialName("fin")
    val fin: String = "",

    @SerialName("estado")
    val estado: String = "",

    @SerialName("especialidad")
    val especialidad: String = "",

    @SerialName("medico")
    val medico: String = "",

    @SerialName("consultorio")
    val consultorio: String? = null
)

@Serializable
data class Especialidad(

    @SerialName("id_especialidad")
    val idEspecialidad: Int = 0,

    @SerialName("nombre")
    val nombre: String = "",

    @SerialName("descripcion")
    val descripcion: String = ""
)

@Serializable
data class MedicoExplorar(

    @SerialName("id_empleado")
    val idEmpleado: Int = 0,

    @SerialName("nombre")
    val nombre: String = "",

    @SerialName("cedula_profesional")
    val cedulaProfesional: String = "",

    @SerialName("especialidades")
    val especialidades: String = ""
)

package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataMedicosEspecialidad(

    @SerialName("success")
    val success: Boolean = false,

    @SerialName("especialidad")
    val especialidad: Especialidad? = null,

    @SerialName("medicos")
    val medicos: List<MedicoEspecialidad> = emptyList(),

    @SerialName("mensaje")
    val mensaje: String = ""
)

@Serializable
data class MedicoEspecialidad(

    @SerialName("id_empleado")
    val idEmpleado: Int = 0,

    @SerialName("nombre")
    val nombre: String = "",

    @SerialName("cedula_profesional")
    val cedulaProfesional: String = "",

    @SerialName("horarios")
    val horarios: List<HorarioMedico> = emptyList(),

    @SerialName("proximo_horario")
    val proximoHorario: ProximoHorario? = null
)

@Serializable
data class HorarioMedico(

    @SerialName("id_horario")
    val idHorario: Int = 0,

    @SerialName("dia_semana")
    val diaSemana: Int = 0,

    @SerialName("hora_inicio")
    val horaInicio: String = "",

    @SerialName("hora_fin")
    val horaFin: String = "",

    @SerialName("intervalo_minutos")
    val intervaloMinutos: Int = 0,

    @SerialName("vigencia_desde")
    val vigenciaDesde: String = "",

    @SerialName("vigencia_hasta")
    val vigenciaHasta: String? = null,

    @SerialName("id_consultorio")
    val idConsultorio: Int? = null,

    @SerialName("consultorio")
    val consultorio: String? = null
)

@Serializable
data class ProximoHorario(

    @SerialName("fecha")
    val fecha: String = "",

    @SerialName("hora")
    val hora: String = "",

    @SerialName("inicio")
    val inicio: String = "",

    @SerialName("fin")
    val fin: String = "",

    @SerialName("consultorio")
    val consultorio: String? = null
)

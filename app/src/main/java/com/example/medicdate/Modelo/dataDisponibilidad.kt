package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Request for availability (date, doctor, specialty, user)
@Serializable
data class DisponibilidadRequest(
    @SerialName("id_empleado") val id_empleado: Int?,
    @SerialName("id_tipo_atencion") val id_tipo_atencion: Int,
    @SerialName("fecha") val fecha: String, // format YYYY-MM-DD
    @SerialName("id_usuario") val id_usuario: Int? = null
)

// Response containing a list of time slots (horario) and doctor info
@Serializable
data class DisponibilidadResponse(
    @SerialName("exito") val exito: Boolean = false,
    @SerialName("mensaje") val mensaje: String? = null,
    @SerialName("codigo") val codigo: String? = null,
    @SerialName("medico") val medico: String? = null,
    @SerialName("tipo_atencion") val tipo_atencion: String? = null,
    @SerialName("especialidad") val especialidad: String? = null,
    @SerialName("duracion_minutos") val duracion_minutos: Int = 0,
    @SerialName("dias_atencion") val dias_atencion: List<Int> = emptyList(),
    @SerialName("horarios") val horarios: List<Horario> = emptyList()
)

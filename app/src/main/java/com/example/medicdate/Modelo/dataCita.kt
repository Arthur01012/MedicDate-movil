package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CitaPaciente(
    @SerialName("id") val id: Int = 0,
    @SerialName("fecha") val fecha: String = "",
    @SerialName("hora") val hora: String = "",
    @SerialName("estado") val estado: String = "",
    @SerialName("medico") val medico: String = "",
    @SerialName("especialidad") val especialidad: String? = null,
    @SerialName("tipo") val tipo: String = "" // "proxima" or "historial"
)

@Serializable
data class DetalleCita(
    @SerialName("id") val id: Int = 0,
    @SerialName("fecha") val fecha: String = "",
    @SerialName("hora") val hora: String = "",
    @SerialName("duracion") val duracion: Int = 0,
    @SerialName("estado") val estado: String = "",
    @SerialName("tipo_atencion") val tipoAtencion: String? = null,
    @SerialName("consultorio") val consultorio: String? = null,
    @SerialName("medico") val medico: String = "",
    @SerialName("especialidad") val especialidad: String? = null
)

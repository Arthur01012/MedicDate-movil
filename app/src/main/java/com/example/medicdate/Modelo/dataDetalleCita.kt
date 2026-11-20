package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataDetalleCita(
    @SerialName("id") val id: Int = 0,
    @SerialName("fecha") val fecha: String = "",
    @SerialName("hora") val hora: String = "",
    @SerialName("duracion") val duracion: Int = 0,
    @SerialName("estado") val estado: String = "",
    @SerialName("tipo_atencion") val tipoAtencion: String = "",
    @SerialName("consultorio") val consultorio: String = "",
    @SerialName("medico") val medico: String = "",
    @SerialName("especialidad") val especialidad: String = ""
)

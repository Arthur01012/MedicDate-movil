package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Horario(
    @SerialName("id_horario") val id_horario: Int = 0,
    @SerialName("hora_inicio") val hora_inicio: String = "",
    @SerialName("hora_fin") val hora_fin: String = "",
    @SerialName("hora") val hora: String = "",
    @SerialName("inicio") val inicio: String = "",
    @SerialName("fin") val fin: String = "",
    @SerialName("duracion_minutos") val duracion_minutos: Int = 0,
    @SerialName("id_consultorio") val id_consultorio: Int? = null,
    @SerialName("consultorio") val consultorio: String? = null,
    @SerialName("disponible") val disponible: Boolean = true
)

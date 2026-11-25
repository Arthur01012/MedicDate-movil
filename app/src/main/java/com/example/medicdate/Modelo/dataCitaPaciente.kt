package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataCitaPaciente(
    @SerialName("id") val id: Int = 0,
    @SerialName("fecha") val fecha: String = "",
    @SerialName("hora") val hora: String = "",
    @SerialName("estado") val estado: String = "",
    @SerialName("medico") val medico: String = "",
    @SerialName("especialidad") val especialidad: String = "",
    @SerialName("tipo") val tipo: String = ""
)

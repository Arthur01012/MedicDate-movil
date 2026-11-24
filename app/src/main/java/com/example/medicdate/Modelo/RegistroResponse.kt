package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegistroResponse(
    @SerialName("exito") val exito: Boolean = false,
    @SerialName("mensaje") val mensaje: String = "",
    @SerialName("idUsuario") val idUsuario: Int = 0,
    @SerialName("idPaciente") val idPaciente: Int = 0
)

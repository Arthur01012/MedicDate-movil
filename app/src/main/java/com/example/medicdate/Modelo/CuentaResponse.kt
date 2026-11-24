package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CuentaResponse(
    @SerialName("exito") val exito: Boolean = false,
    @SerialName("mensaje") val mensaje: String = "",
    @SerialName("nombre") val nombre: String = "",
    @SerialName("correo") val correo: String = ""
)

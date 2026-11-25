package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecuperarRequest(
    @SerialName("correo") val correo: String = "",
    @SerialName("respuesta") val respuesta: String = "",
    @SerialName("claveNueva") val claveNueva: String = ""
)

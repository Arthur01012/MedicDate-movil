package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CorreoRecuperarRequest(
    @SerialName("correo") val correo: String = ""
)

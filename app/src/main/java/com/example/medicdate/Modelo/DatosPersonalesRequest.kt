package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DatosPersonalesRequest(
    @SerialName("idUsuario") val idUsuario: Int = 0,
    @SerialName("nombres") val nombres: String = "",
    @SerialName("apellidoPaterno") val apellidoPaterno: String = "",
    @SerialName("apellidoMaterno") val apellidoMaterno: String = "",
    @SerialName("telefono") val telefono: String = ""
)
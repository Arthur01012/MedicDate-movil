package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataDatosPersonales(
    @SerialName("exito") val exito: Boolean = false,
    @SerialName("mensaje") val mensaje: String = "",
    @SerialName("nombres") val nombres: String = "",
    @SerialName("apellidoPaterno") val apellidoPaterno: String = "",
    @SerialName("apellidoMaterno") val apellidoMaterno: String = "",
    @SerialName("telefono") val telefono: String = "",
    @SerialName("correo") val correo: String = ""
)



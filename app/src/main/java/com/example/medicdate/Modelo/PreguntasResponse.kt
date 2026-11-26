package com.example.medicdate.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataPregunta(
    @SerialName("idPregunta") val idPregunta: Int = 0,
    @SerialName("texto") val texto: String = ""
) {
    override fun toString(): String {
        return texto
    }
}

@Serializable
data class PreguntasResponse(
    @SerialName("exito") val exito: Boolean = false,
    @SerialName("mensaje") val mensaje: String = "",
    @SerialName("preguntas") val preguntas: List<dataPregunta> = emptyList()
)
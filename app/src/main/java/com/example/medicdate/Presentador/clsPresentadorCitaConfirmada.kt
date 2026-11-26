package com.example.medicdate.Presentador

import com.example.medicdate.Vista.CitaConfirmadaActivity

class clsPresentadorCitaConfirmada(private val vista: CitaConfirmadaActivity) {
    fun mostrarCita(idCita: Int) {
        vista.mostrarDetalleId(idCita)
    }
}

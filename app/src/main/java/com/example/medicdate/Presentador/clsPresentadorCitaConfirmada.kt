package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.ReservaRequest
import com.example.medicdate.Vista.CitaConfirmadaActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Placeholder presenter for the confirmed screen. If you need to fetch full appointment details,
// you can add a corresponding model method (e.g., clsCitaDetalleModelo) and call it here.
class clsPresentadorCitaConfirmada(private val vista: CitaConfirmadaActivity) {
    fun mostrarCita(idCita: Int) {
        // Directly pass the ID to the view; replace with a network call if required.
        vista.mostrarDetalleId(idCita)
    }
}

package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsMisCitasModelo
import com.example.medicdate.Vista.DetalleCitaActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class clsPresentadorDetalleCita(private val vista: DetalleCitaActivity) {
    private val modelo = clsMisCitasModelo()
    private var idCita = 0
    private var idUsuario = 0
    private val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    private val meses = listOf("enero", "febrero", "marzo", "abril", "mayo", "junio", "julio",
        "agosto", "septiembre", "octubre", "noviembre", "diciembre")

    fun cargarDetalle(id: Int, idUser: Int)
    {
        idCita = id
        idUsuario = idUser
        // Si no llego el id, se regresa al login
        if (idUser == 0)
        {
            return vista.irALogin()
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val d = modelo.obtenerDetalle(id, idUser)
                val fecha = LocalDate.parse(d.fecha)
                val hora = d.hora.take(5)
                val titulo = dias[fecha.dayOfWeek.value - 1] + " " + fecha.dayOfMonth + " de " + meses[fecha.monthValue - 1]
                val horario = hora + " · " + d.duracion + " min"
                val atencion = d.especialidad + " · " + d.tipoAtencion

                // Solo se puede cancelar si faltan 24 horas o mas
                val inicio = LocalDateTime.parse(d.fecha + "T" + hora)
                val sePuedeCancelar = LocalDateTime.now().plusHours(24).isBefore(inicio)

                vista.mostrarDetalle(d.estado, titulo, horario, d.medico, atencion, d.consultorio, sePuedeCancelar)
            } catch (e: Exception)
            {
                vista.mostrarError("Error al cargar la cita: " + e.localizedMessage)
            }
        }
    }

    fun cancelarCita()
    {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.cancelarCita(idCita, idUsuario)
                if (respuesta.exito)
                {
                    vista.cancelacionExitosa(respuesta.mensaje)
                }
                else
                {
                    vista.mostrarError(respuesta.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("No se pudo cancelar la cita: " + e.localizedMessage)
            }
        }
    }
}

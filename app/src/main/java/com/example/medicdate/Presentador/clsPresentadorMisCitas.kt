package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsMisCitasModelo
import com.example.medicdate.Modelo.dataCitaPaciente
import com.example.medicdate.Vista.MisCitasActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorMisCitas(private val vista: MisCitasActivity) {
    private val modelo = clsMisCitasModelo()
    private var citas: List<dataCitaPaciente> = emptyList()

    fun cargarCitas(idUsuario: Int)
    {
        // Si no llego el id, se regresa al login
        if (idUsuario == 0)
        {
            return vista.irALogin()
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                citas = modelo.obtenerCitas(idUsuario)
                mostrarProximas()
            } catch (e: Exception)
            {
                vista.mostrarError("Error al cargar las citas: " + e.localizedMessage)
            }
        }
    }

    fun mostrarProximas()
    {
        // La mas cercana primero
        val lista = citas.filter { it.tipo == "proxima" }.sortedBy { it.fecha + it.hora }
        vista.marcarPestana(true)
        if (lista.isEmpty())
        {
            vista.mostrarVacio("No tienes citas próximas")
        }
        else
        {
            vista.mostrarCitas(lista)
        }
    }

    fun mostrarHistorial()
    {
        val lista = citas.filter { it.tipo == "historial" }
        vista.marcarPestana(false)
        if (lista.isEmpty())
        {
            vista.mostrarVacio("No tienes citas en el historial")
        }
        else
        {
            vista.mostrarCitas(lista)
        }
    }
}

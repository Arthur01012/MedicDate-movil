package com.example.medicdate.Presentador

import com.example.medicdate.Modelo.clsCuentaModelo
import com.example.medicdate.Vista.CuentaActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPresentadorCuenta(private val vista: CuentaActivity) {
    private val modelo = clsCuentaModelo()

    fun cargarCuenta(idUsuario: Int)
    {
        // Si no llego el id, se regresa al login
        if (idUsuario == 0)
        {
            vista.irALogin()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val cuenta = modelo.obtenerCuenta(idUsuario)
                if (cuenta.exito)
                {
                    vista.mostrarCuenta(cuenta.nombre, cuenta.correo)
                }
                else
                {
                    vista.mostrarError(cuenta.mensaje)
                }
            } catch (e: Exception)
            {
                vista.mostrarError("Error al cargar la cuenta: " + e.localizedMessage)
            }
        }
    }
}

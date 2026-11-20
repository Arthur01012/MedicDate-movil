package com.example.medicdate.Vista

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicdate.Modelo.MedicoEspecialidad
import com.example.medicdate.R
import java.text.SimpleDateFormat
import java.util.Locale

class MedicoEspecialidadAdapter(
    private var lista: List<MedicoEspecialidad>,
    private var nombreEspecialidad: String,
    private val alSeleccionar: (MedicoEspecialidad) -> Unit
) : RecyclerView.Adapter<MedicoEspecialidadAdapter.MedicoEspecialidadViewHolder>() {

    class MedicoEspecialidadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtIniciales: TextView = itemView.findViewById(R.id.txtIniciales)
        val txtNombreMedico: TextView = itemView.findViewById(R.id.txtNombreMedico)
        val txtSubtitulo: TextView = itemView.findViewById(R.id.txtSubtitulo)
        val txtProximoHorario: TextView = itemView.findViewById(R.id.txtProximoHorario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicoEspecialidadViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medico_especialidad, parent, false)
        return MedicoEspecialidadViewHolder(vista)
    }

    override fun onBindViewHolder(holder: MedicoEspecialidadViewHolder, position: Int) {
        val medico = lista[position]

        holder.txtIniciales.text = generarIniciales(medico.nombre)
        holder.txtNombreMedico.text = medico.nombre

        val consultorio = medico.proximoHorario?.consultorio
            ?: medico.horarios.firstOrNull()?.consultorio

        holder.txtSubtitulo.text = if (!consultorio.isNullOrBlank()) {
            "$nombreEspecialidad · $consultorio"
        } else {
            nombreEspecialidad
        }

        val proximo = medico.proximoHorario
        holder.txtProximoHorario.text = if (proximo != null) {
            "Próximo horario: ${formatearFechaCorta(proximo.fecha)} · ${proximo.hora}"
        } else {
            "Sin horarios disponibles"
        }

        holder.itemView.setOnClickListener { alSeleccionar(medico) }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarDatos(nuevaLista: List<MedicoEspecialidad>, especialidad: String) {
        lista = nuevaLista
        nombreEspecialidad = especialidad
        notifyDataSetChanged()
    }

    private fun generarIniciales(nombre: String): String {
        val partes = nombre.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        if (partes.isEmpty()) return "MD"
        val primera = partes.first().first().uppercaseChar()
        val segunda = if (partes.size > 1) partes.last().first().uppercaseChar() else null
        return if (segunda != null) "$primera$segunda" else "$primera"
    }

    private fun formatearFechaCorta(fechaIso: String): String {
        return try {
            val entrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fecha = entrada.parse(fechaIso) ?: return fechaIso
            val salida = SimpleDateFormat("EEE d MMM", Locale("es", "MX"))
            salida.format(fecha).replace(".", "")
        } catch (_: Exception) {
            fechaIso
        }
    }
}
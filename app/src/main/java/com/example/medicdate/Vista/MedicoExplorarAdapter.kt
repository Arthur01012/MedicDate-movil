package com.example.medicdate.Vista

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicdate.Modelo.MedicoExplorar
import com.example.medicdate.R
import java.util.Locale

class MedicoExplorarAdapter(
    private var lista: List<MedicoExplorar>,
    private val alSeleccionar: (MedicoExplorar) -> Unit
) : RecyclerView.Adapter<MedicoExplorarAdapter.MedicoViewHolder>() {

    class MedicoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtIniciales: TextView = itemView.findViewById(R.id.txtIniciales)
        val txtNombreMedico: TextView = itemView.findViewById(R.id.txtNombreMedico)
        val txtEspecialidadesMedico: TextView = itemView.findViewById(R.id.txtEspecialidadesMedico)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medico_explorar, parent, false)
        return MedicoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: MedicoViewHolder, position: Int) {
        val medico = lista[position]
        holder.txtIniciales.text = generarIniciales(medico.nombre)
        holder.txtNombreMedico.text = medico.nombre
        holder.txtEspecialidadesMedico.text = medico.especialidades
        holder.itemView.setOnClickListener { alSeleccionar(medico) }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarDatos(nuevaLista: List<MedicoExplorar>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    private fun generarIniciales(nombre: String): String {
        val partes = nombre.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        if (partes.isEmpty()) return "MD"
        val primera = partes.first().first().uppercaseChar()
        val segunda = if (partes.size > 1) partes.last().first().uppercaseChar() else null
        return if (segunda != null) "$primera$segunda" else "$primera"
    }
}
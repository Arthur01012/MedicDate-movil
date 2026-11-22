package com.example.medicdate.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicdate.Modelo.Especialidad
import com.example.medicdate.R

class EspecialidadAdapter(
    private var lista: List<Especialidad>,
    private val alSeleccionar: (Especialidad) -> Unit
) : RecyclerView.Adapter<EspecialidadAdapter.EspecialidadViewHolder>() {

    class EspecialidadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombreEspecialidad: TextView = itemView.findViewById(R.id.txtNombreEspecialidad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspecialidadViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_especialidad, parent, false)
        return EspecialidadViewHolder(vista)
    }

    override fun onBindViewHolder(holder: EspecialidadViewHolder, position: Int) {
        val especialidad = lista[position]
        holder.txtNombreEspecialidad.text = especialidad.nombre
        holder.itemView.setOnClickListener { alSeleccionar(especialidad) }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarDatos(nuevaLista: List<Especialidad>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
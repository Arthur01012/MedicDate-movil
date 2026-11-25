package com.example.medicdate.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicdate.Modelo.dataCitaPaciente
import com.example.medicdate.R
import com.example.medicdate.Vista.MisCitasActivity

class CitasAdaptador(
    private val activity: MisCitasActivity,
    private val lista: List<dataCitaPaciente>
) :
    RecyclerView.Adapter<CitasAdaptador.CitaViewHolder>() {

    private val meses = listOf("ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC")

    class CitaViewHolder(vista: View) : RecyclerView.ViewHolder(vista) {
        val txtDia: TextView = vista.findViewById(R.id.txtDia)
        val txtMes: TextView = vista.findViewById(R.id.txtMes)
        val txtEspecialidad: TextView = vista.findViewById(R.id.txtEspecialidad)
        val txtMedicoHora: TextView = vista.findViewById(R.id.txtMedicoHora)
        val txtEstado: TextView = vista.findViewById(R.id.txtEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = lista[position]

        // La fecha llega como 2026-11-13
        if (cita.fecha.length >= 10)
        {
            holder.txtDia.text = cita.fecha.substring(8, 10)
            holder.txtMes.text = meses[cita.fecha.substring(5, 7).toInt() - 1]
        }

        // La especialidad puede venir vacia
        holder.txtEspecialidad.text = if (cita.especialidad.isEmpty()) "Consulta" else cita.especialidad
        holder.txtMedicoHora.text = cita.medico + " · " + cita.hora
        holder.txtEstado.text = cita.estado

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                activity.verDetalleCita(cita)
            }
        })
    }

    override fun getItemCount(): Int = lista.size
}

package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicdate.Modelo.Horario
import com.example.medicdate.Presentador.clsPresentadorReservaCita
import com.example.medicdate.R
import com.example.medicdate.UI.WeekSwipeLayout
import com.example.medicdate.Util.SessionManager
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class RecervarCitaActivity : AppCompatActivity() {
    private lateinit var presenter: clsPresentadorReservaCita
    private var doctorId: Int = 2
    private var tipoAtencionId: Int = 1
    private var doctorName: String = "Dra. Laura Méndez"
    private var doctorSpecialty: String = "Cardiología"

    private lateinit var weekSwipe: WeekSwipeLayout
    private lateinit var weekLabel: TextView
    private lateinit var rvHorarios: RecyclerView
    private lateinit var skeletonLoading: View
    private lateinit var groupTimes: View
    private lateinit var radioGroupTipo: RadioGroup
    private lateinit var summaryText: TextView
    private lateinit var btnContinue: MaterialButton

    private var selectedDate: String = "" // format YYYY-MM-DD
    private var selectedHorario: Horario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recervar_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        SessionManager.init(this)

        presenter = clsPresentadorReservaCita(this)
        val passedDoctorId = intent.getIntExtra("doctorId", -1)
        if (passedDoctorId != -1) doctorId = passedDoctorId

        val passedName = intent.getStringExtra("doctorName")
        if (!passedName.isNullOrEmpty()) {
            doctorName = passedName
        }

        val passedSpec = intent.getStringExtra("doctorSpecialty")
        if (!passedSpec.isNullOrEmpty()) {
            doctorSpecialty = passedSpec
        }

        val tipoExtra = intent.getIntExtra("tipoAtencionId", 1)
        if (tipoExtra != -1) tipoAtencionId = tipoExtra

        inicializarVistas()
        configurarEventos()

        selectedDate = weekSwipe.currentDateString
        actualizarEtiquetaSemana()
        cargarDisponibilidad()
    }

    private fun inicializarVistas() {
        findViewById<View>(R.id.button_back).setOnClickListener { finish() }

        val txtInitials = findViewById<TextView>(R.id.txtDoctorInitials)
        val txtName = findViewById<TextView>(R.id.txtDoctorName)
        val txtSpecialty = findViewById<TextView>(R.id.txtDoctorSpecialtyLocation)

        if (txtInitials != null) txtInitials.text = generarIniciales(doctorName)
        if (txtName != null) txtName.text = doctorName
        if (txtSpecialty != null) txtSpecialty.text = "$doctorSpecialty · Consultorio"

        weekSwipe = findViewById(R.id.WeekSwipeLayout)
        weekLabel = findViewById(R.id.week_label)
        rvHorarios = findViewById(R.id.rvHorarios)
        skeletonLoading = findViewById<View>(R.id.skeleton_loading)
        groupTimes = findViewById(R.id.group_times)
        radioGroupTipo = findViewById(R.id.group_appointment_type)
        summaryText = findViewById(R.id.selected_appointment_summary)
        btnContinue = findViewById(R.id.button_continue)

        groupTimes.visibility = View.GONE
        rvHorarios.visibility = View.GONE
        skeletonLoading.visibility = View.VISIBLE

        actualizarSombreadoTipoAtencion(radioGroupTipo.checkedRadioButtonId)

        rvHorarios.layoutManager = GridLayoutManager(this, 3)
        rvHorarios.adapter = HorarioAdapter(emptyList()) { horario ->
            seleccionarHorario(horario)
        }
    }

    private fun configurarEventos() {
        findViewById<View>(R.id.button_previous_week).setOnClickListener {
            weekSwipe.changeWeek(-1)
            actualizarEtiquetaSemana()
        }

        findViewById<View>(R.id.button_next_week).setOnClickListener {
            weekSwipe.changeWeek(1)
            actualizarEtiquetaSemana()
        }

        weekSwipe.setOnDateChangeListener { newDate ->
            selectedDate = newDate
            selectedHorario = null
            actualizarEtiquetaSemana()
            actualizarResumen()
            cargarDisponibilidad()
        }

        weekSwipe.onWeekSwipe = { _ ->
            actualizarEtiquetaSemana()
        }

        radioGroupTipo.setOnCheckedChangeListener { _, checkedId ->
            tipoAtencionId = if (checkedId == R.id.radio_follow_up) 2 else 1
            actualizarSombreadoTipoAtencion(checkedId)
            selectedHorario = null
            actualizarResumen()
            cargarDisponibilidad()
        }

        btnContinue.setOnClickListener {
            val horario = selectedHorario
            if (horario == null) {
                Toast.makeText(this, "Por favor selecciona un horario disponible", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val horaSlot = if (horario.hora.isNotEmpty()) horario.hora else horario.hora_inicio
            val fechaHoraIso = "${selectedDate}T${horaSlot}:00"
            val tipoNombre = if (tipoAtencionId == 2) "Seguimiento · 20 min" else "Primera consulta · 30 min"

            val intent = Intent(this, ConfirmarCitaActivity::class.java).apply {
                putExtra("doctorId", doctorId)
                putExtra("tipoAtencionId", tipoAtencionId)
                putExtra("fechaHora", fechaHoraIso)
                putExtra("doctorName", doctorName)
                putExtra("tipoAtencionNombre", tipoNombre)
            }
            startActivity(intent)
        }
    }

    private fun actualizarSombreadoTipoAtencion(checkedId: Int) {
        val radioFirst = findViewById<RadioButton>(R.id.radio_first_visit)
        val radioFollow = findViewById<RadioButton>(R.id.radio_follow_up)
        if (radioFirst != null && radioFollow != null) {
            if (checkedId == R.id.radio_follow_up) {
                radioFirst.setBackgroundResource(R.drawable.bg_option_default)
                radioFollow.setBackgroundResource(R.drawable.bg_option_selected)
            } else {
                radioFirst.setBackgroundResource(R.drawable.bg_option_selected)
                radioFollow.setBackgroundResource(R.drawable.bg_option_default)
            }
        }
    }

    private fun generarIniciales(nombre: String): String {
        val partes = nombre.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        if (partes.isEmpty()) return "MD"
        val primera = partes.first().first().uppercaseChar()
        val segunda = if (partes.size > 1) partes.last().first().uppercaseChar() else null
        return if (segunda != null) "$primera$segunda" else "$primera"
    }

    private fun actualizarEtiquetaSemana() {
        weekLabel.text = weekSwipe.getWeekRangeLabel()
    }

    private fun cargarDisponibilidad() {
        startSkeletonAnimation()
        val usrId = if (SessionManager.usuarioId != -1) SessionManager.usuarioId else null
        presenter.cargarDisponibilidad(doctorId, tipoAtencionId, selectedDate, usrId)
    }

    fun mostrarHorarios(horarios: List<Horario>) {
        stopSkeletonAnimation()
        (rvHorarios.adapter as HorarioAdapter).updateData(horarios)
        if (horarios.isEmpty()) {
            summaryText.text = "No hay horarios disponibles para esta fecha"
        }
    }

    fun mostrarError(msg: String) {
        stopSkeletonAnimation()
        summaryText.text = msg
    }

    private fun startSkeletonAnimation() {
        skeletonLoading.visibility = View.VISIBLE
        rvHorarios.visibility = View.GONE
        val anim = AlphaAnimation(0.25f, 0.85f).apply {
            duration = 450
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        skeletonLoading.startAnimation(anim)
    }

    private fun stopSkeletonAnimation() {
        skeletonLoading.clearAnimation()
        skeletonLoading.visibility = View.GONE
        rvHorarios.visibility = View.VISIBLE
    }

    private fun seleccionarHorario(horario: Horario) {
        selectedHorario = horario
        actualizarResumen()
    }

    private fun actualizarResumen() {
        val horario = selectedHorario
        if (horario == null) {
            summaryText.text = "Selecciona una fecha y hora para continuar"
            return
        }

        val fechaBonita = formatearFechaCorta(selectedDate)
        val horaTexto = if (horario.hora.isNotEmpty()) horario.hora else horario.hora_inicio
        val tipoTexto = if (tipoAtencionId == 2) "Seguimiento" else "Primera consulta"
        summaryText.text = "$fechaBonita · $horaTexto · $tipoTexto"
    }

    private fun formatearFechaCorta(fechaIso: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = parser.parse(fechaIso) ?: return fechaIso
            val formatter = SimpleDateFormat("EEE d MMM", Locale("es", "MX"))
            formatter.format(date).replace(".", "")
        } catch (_: Exception) {
            fechaIso
        }
    }

    private class HorarioAdapter(
        private var items: List<Horario>,
        private val onClick: (Horario) -> Unit
    ) : RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>() {

        private var selectedPosition: Int = -1

        inner class HorarioViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val tvHora: TextView = view.findViewById(R.id.tvHorario)
            val btnSelect: View = view.findViewById(R.id.btnSelect)

            fun bind(horario: Horario, isSelected: Boolean) {
                val horaTexto = if (horario.hora.isNotEmpty()) horario.hora else horario.hora_inicio
                tvHora.text = horaTexto
                val disponible = horario.disponible

                if (!disponible) {
                    view.alpha = 0.35f
                    view.isEnabled = false
                    btnSelect.isEnabled = false
                    tvHora.setTextColor(ContextCompat.getColor(view.context, R.color.text_secondary))
                    view.setBackgroundResource(R.drawable.bg_time_default)
                    view.setOnClickListener(null)
                } else {
                    view.alpha = 1.0f
                    view.isEnabled = true
                    btnSelect.isEnabled = true

                    if (isSelected) {
                        tvHora.setTextColor(ContextCompat.getColor(view.context, R.color.on_brand))
                        view.setBackgroundResource(R.drawable.bg_time_selected)
                    } else {
                        tvHora.setTextColor(ContextCompat.getColor(view.context, R.color.text_primary))
                        view.setBackgroundResource(R.drawable.bg_time_default)
                    }

                    view.setOnClickListener {
                        val prev = selectedPosition
                        selectedPosition = bindingAdapterPosition
                        if (prev != RecyclerView.NO_POSITION) notifyItemChanged(prev)
                        notifyItemChanged(selectedPosition)
                        onClick(horario)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_horario, parent, false)
            return HorarioViewHolder(v)
        }

        override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
            holder.bind(items[position], position == selectedPosition)
        }

        override fun getItemCount(): Int = items.size

        fun updateData(newItems: List<Horario>) {
            items = newItems
            selectedPosition = -1
            notifyDataSetChanged()
        }
    }
}

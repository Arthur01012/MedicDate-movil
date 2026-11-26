package com.example.medicdate.Vista

import android.content.Intent
import android.graphics.Paint
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
import androidx.core.view.OnApplyWindowInsetsListener
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

class RecervarCitaActivity : AppCompatActivity(),
    View.OnClickListener,
    OnApplyWindowInsetsListener,
    RadioGroup.OnCheckedChangeListener,
    WeekSwipeLayout.OnDateChangeListener,
    WeekSwipeLayout.OnWeekSwipeListener,
    RecervarCitaActivity.OnHorarioSeleccionadoListener {

    interface OnHorarioSeleccionadoListener {
        fun onHorarioSeleccionado(horario: Horario)
    }

    private lateinit var presenter: clsPresentadorReservaCita
    private var doctorId: Int = 2
    private var tipoAtencionId: Int = 1
    private var doctorName: String = "Dra. Laura Méndez"
    private var doctorSpecialty: String = "Cardiología"

    private lateinit var btnBack: View
    private lateinit var btnPreviousWeek: View
    private lateinit var btnNextWeek: View
    private lateinit var weekSwipe: WeekSwipeLayout
    private lateinit var weekLabel: TextView
    private lateinit var rvHorarios: RecyclerView
    private lateinit var horarioAdapter: HorarioAdapter
    private lateinit var skeletonLoading: View
    private lateinit var groupTimes: View
    private lateinit var radioGroupTipo: RadioGroup
    private lateinit var summaryText: TextView
    private lateinit var btnContinue: MaterialButton

    private var selectedDate: String = ""
    private var selectedHorario: Horario? = null
    private var primeraCargaCompletada: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recervar_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), this)

        SessionManager.init(this)

        presenter = clsPresentadorReservaCita(this)
        val passedDoctorId = intent.getIntExtra(EXTRA_DOCTOR_ID, -1)
        if (passedDoctorId != -1) {
            doctorId = passedDoctorId
        }

        val passedName = intent.getStringExtra(EXTRA_DOCTOR_NAME).orEmpty()
        if (passedName.isNotBlank()) {
            doctorName = passedName
        }

        val passedSpec = intent.getStringExtra(EXTRA_DOCTOR_SPECIALTY).orEmpty()
        if (passedSpec.isNotBlank()) {
            doctorSpecialty = passedSpec
        }

        val tipoExtra = intent.getIntExtra(EXTRA_TIPO_ATENCION_ID, 1)
        if (tipoExtra != -1) {
            tipoAtencionId = tipoExtra
        }

        inicializarVistas()
        configurarEventos()

        selectedDate = weekSwipe.currentDateString
        actualizarEtiquetaSemana()
        actualizarResumen()
        cargarDisponibilidad()
    }

    override fun onResume() {
        super.onResume()
        if (primeraCargaCompletada) {
            selectedHorario = null
            actualizarResumen()
            cargarDisponibilidad()
        }
    }

    override fun onDestroy() {
        if (::presenter.isInitialized) {
            presenter.cancelarSolicitudes()
        }
        super.onDestroy()
    }

    override fun onApplyWindowInsets(
        view: View,
        insets: WindowInsetsCompat
    ): WindowInsetsCompat {
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        return insets
    }

    private fun inicializarVistas() {
        btnBack = findViewById(R.id.button_back)
        btnPreviousWeek = findViewById(R.id.button_previous_week)
        btnNextWeek = findViewById(R.id.button_next_week)

        val txtInitials = findViewById<TextView>(R.id.txtDoctorInitials)
        val txtName = findViewById<TextView>(R.id.txtDoctorName)
        val txtSpecialty = findViewById<TextView>(R.id.txtDoctorSpecialtyLocation)

        if (txtInitials != null) {
            txtInitials.text = generarIniciales(doctorName)
        }
        if (txtName != null) {
            txtName.text = doctorName
        }
        if (txtSpecialty != null) {
            txtSpecialty.text = "$doctorSpecialty · Consultorio"
        }

        weekSwipe = findViewById(R.id.WeekSwipeLayout)
        weekLabel = findViewById(R.id.week_label)
        rvHorarios = findViewById(R.id.rvHorarios)
        skeletonLoading = findViewById(R.id.skeleton_loading)
        groupTimes = findViewById(R.id.group_times)
        radioGroupTipo = findViewById(R.id.group_appointment_type)
        summaryText = findViewById(R.id.selected_appointment_summary)
        btnContinue = findViewById(R.id.button_continue)

        groupTimes.visibility = View.GONE
        rvHorarios.visibility = View.GONE
        skeletonLoading.visibility = View.VISIBLE

        actualizarSombreadoTipoAtencion(radioGroupTipo.checkedRadioButtonId)

        horarioAdapter = HorarioAdapter(emptyList(), this)
        rvHorarios.layoutManager = GridLayoutManager(this, 3)
        rvHorarios.adapter = horarioAdapter
    }

    override fun onHorarioSeleccionado(horario: Horario) {
        seleccionarHorario(horario)
    }

    private fun configurarEventos() {
        btnBack.setOnClickListener(this)
        btnPreviousWeek.setOnClickListener(this)
        btnNextWeek.setOnClickListener(this)
        btnContinue.setOnClickListener(this)
        radioGroupTipo.setOnCheckedChangeListener(this)
        weekSwipe.setOnDateChangeListener(this)
        weekSwipe.setOnWeekSwipeListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_back -> finish()
            R.id.button_previous_week -> {
                weekSwipe.changeWeek(-1)
                actualizarEtiquetaSemana()
            }
            R.id.button_next_week -> {
                weekSwipe.changeWeek(1)
                actualizarEtiquetaSemana()
            }
            R.id.button_continue -> abrirConfirmarCita()
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        tipoAtencionId = if (checkedId == R.id.radio_follow_up) 2 else 1
        actualizarSombreadoTipoAtencion(checkedId)
        selectedHorario = null
        actualizarResumen()
        cargarDisponibilidad()
    }

    override fun onDateChanged(newDate: String) {
        selectedDate = newDate
        selectedHorario = null
        actualizarEtiquetaSemana()
        actualizarResumen()
        cargarDisponibilidad()
    }

    override fun onWeekSwiped(direction: Int) {
        actualizarEtiquetaSemana()
    }

    private fun abrirConfirmarCita() {
        val horario = selectedHorario
        if (horario == null) {
            Toast.makeText(this, "Por favor selecciona un horario disponible", Toast.LENGTH_SHORT).show()
            return
        }

        val horaSlot = if (horario.hora.isNotEmpty()) horario.hora else horario.hora_inicio
        val fechaHoraIso = "${selectedDate}T${horaSlot}:00"
        val tipoNombre = if (tipoAtencionId == 2) "Seguimiento · 20 min" else "Primera consulta · 30 min"

        val intentConfirmacion = Intent(this, ConfirmarCitaActivity::class.java)
            .putExtra(ConfirmarCitaActivity.EXTRA_DOCTOR_ID, doctorId)
            .putExtra(ConfirmarCitaActivity.EXTRA_TIPO_ATENCION_ID, tipoAtencionId)
            .putExtra(ConfirmarCitaActivity.EXTRA_FECHA_HORA, fechaHoraIso)
            .putExtra(ConfirmarCitaActivity.EXTRA_DOCTOR_NAME, doctorName)
            .putExtra(ConfirmarCitaActivity.EXTRA_DOCTOR_SPECIALTY, doctorSpecialty)
            .putExtra(ConfirmarCitaActivity.EXTRA_TIPO_ATENCION_NOMBRE, tipoNombre)
            .putExtra(ConfirmarCitaActivity.EXTRA_CONSULTORIO_ID, horario.id_consultorio ?: -1)
            .putExtra(ConfirmarCitaActivity.EXTRA_CONSULTORIO_NOMBRE, horario.consultorio.orEmpty())
        startActivity(intentConfirmacion)
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
        val crudos = nombre.trim().split("\\s+".toRegex())
        val partes = ArrayList<String>()
        for (parte in crudos) {
            if (parte.isNotEmpty()) {
                partes.add(parte)
            }
        }
        if (partes.isEmpty()) {
            return "MD"
        }
        val primera = partes[0][0].uppercaseChar()
        val segunda = if (partes.size > 1) partes[partes.size - 1][0].uppercaseChar() else null
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

    fun mostrarHorarios(
        horarios: List<Horario>,
        diasAtencion: List<Int> = emptyList(),
        mensajeApi: String? = null
    ) {
        val fechaCambio = weekSwipe.setDoctorWorkingDays(diasAtencion.toSet())
        actualizarEtiquetaSemana()

        if (fechaCambio && weekSwipe.tieneDiasDeAtencion()) {
            selectedDate = weekSwipe.currentDateString
            selectedHorario = null
            actualizarResumen()
            cargarDisponibilidad()
            return
        }

        primeraCargaCompletada = true
        stopSkeletonAnimation()
        horarioAdapter.updateData(horarios)

        var disponibles = 0
        for (item in horarios) {
            if (item.disponible) {
                disponibles++
            }
        }

        if (!weekSwipe.tieneDiasDeAtencion()) {
            summaryText.text = "Este médico no tiene días de atención disponibles"
        } else if (horarios.isEmpty() || disponibles == 0) {
            summaryText.text = if (!mensajeApi.isNullOrBlank()) {
                mensajeApi
            } else {
                "No hay horarios disponibles para esta fecha"
            }
        } else {
            actualizarResumen()
        }
    }

    fun mostrarError(msg: String, diasAtencion: List<Int> = emptyList()) {
        if (diasAtencion.isNotEmpty()) {
            val fechaCambio = weekSwipe.setDoctorWorkingDays(diasAtencion.toSet())
            actualizarEtiquetaSemana()
            if (fechaCambio) {
                selectedDate = weekSwipe.currentDateString
                selectedHorario = null
                actualizarResumen()
                cargarDisponibilidad()
                return
            }
        }
        primeraCargaCompletada = true
        stopSkeletonAnimation()
        horarioAdapter.updateData(emptyList())
        summaryText.text = msg
    }

    private fun startSkeletonAnimation() {
        skeletonLoading.visibility = View.VISIBLE
        rvHorarios.visibility = View.GONE
        val anim = AlphaAnimation(0.25f, 0.85f)
        anim.duration = 450
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
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
            val formateado = formatter.format(date).replace(".", "")
            if (formateado.isNotEmpty()) {
                formateado.substring(0, 1).uppercase(Locale("es", "MX")) + formateado.substring(1)
            } else {
                formateado
            }
        } catch (_: Exception) {
            fechaIso
        }
    }

    private class HorarioAdapter(
        private var items: List<Horario>,
        private val listener: OnHorarioSeleccionadoListener
    ) : RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>(), View.OnClickListener {

        private var selectedPosition: Int = -1

        class HorarioViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val tvHora: TextView = view.findViewById(R.id.tvHorario)
            val btnSelect: View = view.findViewById(R.id.btnSelect)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_horario, parent, false)
            return HorarioViewHolder(v)
        }

        override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
            val horario = items[position]
            val isSelected = position == selectedPosition
            val horaTexto = if (horario.hora.isNotEmpty()) horario.hora else horario.hora_inicio
            holder.tvHora.text = horaTexto

            if (!horario.disponible) {
                holder.view.alpha = 0.45f
                holder.view.isEnabled = false
                holder.view.isClickable = false
                holder.btnSelect.isEnabled = false
                holder.tvHora.paintFlags = holder.tvHora.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvHora.setTextColor(
                    ContextCompat.getColor(holder.view.context, R.color.text_secondary)
                )
                holder.view.setBackgroundResource(R.drawable.bg_time_default)
                holder.view.tag = null
                holder.view.setOnClickListener(null)
            } else {
                holder.view.alpha = 1.0f
                holder.view.isEnabled = true
                holder.view.isClickable = true
                holder.btnSelect.isEnabled = true
                holder.tvHora.paintFlags =
                    holder.tvHora.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

                if (isSelected) {
                    holder.tvHora.setTextColor(
                        ContextCompat.getColor(holder.view.context, R.color.on_brand)
                    )
                    holder.view.setBackgroundResource(R.drawable.bg_time_selected)
                } else {
                    holder.tvHora.setTextColor(
                        ContextCompat.getColor(holder.view.context, R.color.text_primary)
                    )
                    holder.view.setBackgroundResource(R.drawable.bg_time_default)
                }

                holder.view.tag = holder
                holder.view.setOnClickListener(this)
            }
        }

        override fun onClick(view: View) {
            val holder = view.tag as? HorarioViewHolder ?: return
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION || pos >= items.size) {
                return
            }
            val horario = items[pos]
            if (!horario.disponible) {
                return
            }

            val prev = selectedPosition
            selectedPosition = pos
            if (prev != RecyclerView.NO_POSITION) {
                notifyItemChanged(prev)
            }
            notifyItemChanged(selectedPosition)
            listener.onHorarioSeleccionado(horario)
        }

        override fun getItemCount(): Int = items.size

        fun updateData(newItems: List<Horario>) {
            items = newItems
            selectedPosition = -1
            notifyDataSetChanged()
        }
    }

    companion object {
        const val EXTRA_DOCTOR_ID = "doctorId"
        const val EXTRA_DOCTOR_NAME = "doctorName"
        const val EXTRA_DOCTOR_SPECIALTY = "doctorSpecialty"
        const val EXTRA_TIPO_ATENCION_ID = "tipoAtencionId"
    }
}

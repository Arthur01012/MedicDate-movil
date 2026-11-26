package com.example.medicdate.Vista

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Modelo.ReservaRequest
import com.example.medicdate.Presentador.clsPresentadorConfirmarCita
import com.example.medicdate.R
import com.example.medicdate.Util.SessionManager
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class ConfirmarCitaActivity : AppCompatActivity(),
    View.OnClickListener,
    OnApplyWindowInsetsListener {

    private lateinit var presenter: clsPresentadorConfirmarCita
    private lateinit var btnBack: View
    private lateinit var btnConfirmar: MaterialButton
    private lateinit var btnElegirOtroHorario: MaterialButton
    private lateinit var btnCambiarHorario: MaterialButton
    private lateinit var conflictContainer: View
    private lateinit var conflictMessageText: TextView
    private lateinit var txtDoctorName: TextView
    private lateinit var txtAppointmentDetails: TextView
    private lateinit var txtDateTime: TextView
    private lateinit var txtClinicLocation: TextView

    private var doctorId: Int = -1
    private var tipoAtencionId: Int = -1
    private var consultorioId: Int = -1
    private var fechaHora: String = ""
    private var doctorName: String = ""
    private var doctorSpecialty: String = ""
    private var tipoAtencionNombre: String = ""
    private var consultorioNombre: String = ""
    private var enviandoReserva: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmar_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), this)

        SessionManager.init(this)

        presenter = clsPresentadorConfirmarCita(this)
        doctorId = intent.getIntExtra(EXTRA_DOCTOR_ID, -1)
        tipoAtencionId = intent.getIntExtra(EXTRA_TIPO_ATENCION_ID, -1)
        consultorioId = intent.getIntExtra(EXTRA_CONSULTORIO_ID, -1)
        fechaHora = intent.getStringExtra(EXTRA_FECHA_HORA).orEmpty()
        doctorName = intent.getStringExtra(EXTRA_DOCTOR_NAME).orEmpty()
        doctorSpecialty = intent.getStringExtra(EXTRA_DOCTOR_SPECIALTY).orEmpty()
        tipoAtencionNombre = intent.getStringExtra(EXTRA_TIPO_ATENCION_NOMBRE).orEmpty()
        consultorioNombre = intent.getStringExtra(EXTRA_CONSULTORIO_NOMBRE).orEmpty()

        if (doctorName.isBlank()) {
            doctorName = "Doctor"
        }
        if (tipoAtencionNombre.isBlank()) {
            tipoAtencionNombre = "Consulta médica"
        }

        inicializarVistas()
        configurarListeners()
        mostrarResumenCita()
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
        btnConfirmar = findViewById(R.id.button_confirm_appointment)
        btnElegirOtroHorario = findViewById(R.id.button_choose_another_time)
        btnCambiarHorario = findViewById(R.id.button_change_time)
        conflictContainer = findViewById(R.id.conflict_message)
        conflictMessageText = findViewById(R.id.conflict_message_text)
        txtDoctorName = findViewById(R.id.confirm_doctor_name)
        txtAppointmentDetails = findViewById(R.id.confirm_appointment_details)
        txtDateTime = findViewById(R.id.confirm_appointment_date_time)
        txtClinicLocation = findViewById(R.id.confirm_clinic_location)
    }

    private fun configurarListeners() {
        btnBack.setOnClickListener(this)
        btnConfirmar.setOnClickListener(this)
        btnElegirOtroHorario.setOnClickListener(this)
        btnCambiarHorario.setOnClickListener(this)
    }

    private fun mostrarResumenCita() {
        txtDoctorName.text = doctorName
        txtAppointmentDetails.text = if (doctorSpecialty.isNotBlank()) {
            "$doctorSpecialty · $tipoAtencionNombre"
        } else {
            tipoAtencionNombre
        }
        txtDateTime.text = formatearFechaHoraBonita(fechaHora)
        txtClinicLocation.text = if (consultorioNombre.isNotBlank()) {
            "$consultorioNombre · Centro Médico Sagrado Corazón"
        } else {
            "Centro Médico Sagrado Corazón"
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_back,
            R.id.button_change_time,
            R.id.button_choose_another_time -> finish()
            R.id.button_confirm_appointment -> confirmar()
        }
    }

    private fun confirmar() {
        if (enviandoReserva) {
            return
        }

        val currentUsuarioId = SessionManager.usuarioId
        if (currentUsuarioId == -1) {
            mostrarError("Debes iniciar sesión para confirmar una cita.")
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
            return
        }

        ocultarConflictoHorario()

        val request = ReservaRequest(
            id_usuario = currentUsuarioId,
            id_empleado = if (doctorId != -1) doctorId else null,
            id_tipo_atencion = if (tipoAtencionId != -1) tipoAtencionId else 1,
            inicio = fechaHora,
            id_consultorio = if (consultorioId != -1) consultorioId else null
        )
        presenter.reservarCita(request)
    }

    fun mostrarCargando(cargando: Boolean) {
        enviandoReserva = cargando
        btnConfirmar.isEnabled = !cargando
        btnCambiarHorario.isEnabled = !cargando
        btnConfirmar.text = if (cargando) {
            "Confirmando..."
        } else {
            getString(R.string.confirm_appointment)
        }
    }

    fun mostrarConflictoHorario(msg: String) {
        val mensajeFinal = if (msg.isNotBlank()) {
            msg
        } else {
            getString(R.string.conflict_message)
        }

        conflictMessageText.text = formatearMensajeConflicto(mensajeFinal)
        conflictContainer.visibility = View.VISIBLE
        btnConfirmar.visibility = View.GONE
        btnCambiarHorario.visibility = View.GONE
        btnElegirOtroHorario.visibility = View.VISIBLE
    }

    private fun ocultarConflictoHorario() {
        conflictContainer.visibility = View.GONE
        btnConfirmar.visibility = View.VISIBLE
        btnCambiarHorario.visibility = View.VISIBLE
        btnElegirOtroHorario.visibility = View.GONE
    }

    private fun formatearMensajeConflicto(mensaje: String): CharSequence {
        val spannable = SpannableString(mensaje)
        val indicePunto = mensaje.indexOf('.')
        val finNegrita = if (indicePunto > 0) indicePunto + 1 else mensaje.length
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            finNegrita,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    fun mostrarError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun irAConfirmada(idCita: Int, consultorioConfirmado: String? = null) {
        val consultorioFinal = if (!consultorioConfirmado.isNullOrBlank()) {
            consultorioConfirmado
        } else {
            consultorioNombre
        }

        val intentConfirmada = Intent(this, CitaConfirmadaActivity::class.java)
            .putExtra(CitaConfirmadaActivity.EXTRA_ID_CITA, idCita)
            .putExtra(CitaConfirmadaActivity.EXTRA_DOCTOR_NAME, doctorName)
            .putExtra(CitaConfirmadaActivity.EXTRA_DOCTOR_SPECIALTY, doctorSpecialty)
            .putExtra(CitaConfirmadaActivity.EXTRA_TIPO_ATENCION_NOMBRE, tipoAtencionNombre)
            .putExtra(CitaConfirmadaActivity.EXTRA_FECHA_HORA, fechaHora)
            .putExtra(CitaConfirmadaActivity.EXTRA_CONSULTORIO_NOMBRE, consultorioFinal)
        startActivity(intentConfirmada)
        finish()
    }

    private fun formatearFechaHoraBonita(fechaHoraIso: String): String {
        return try {
            val isoParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val date = isoParser.parse(fechaHoraIso) ?: return fechaHoraIso
            val formatter = SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy · HH:mm", Locale("es", "MX"))
            val formateado = formatter.format(date)
            if (formateado.isNotEmpty()) {
                formateado.substring(0, 1).uppercase(Locale("es", "MX")) + formateado.substring(1)
            } else {
                formateado
            }
        } catch (_: Exception) {
            fechaHoraIso
        }
    }

    companion object {
        const val EXTRA_DOCTOR_ID = "doctorId"
        const val EXTRA_DOCTOR_NAME = "doctorName"
        const val EXTRA_DOCTOR_SPECIALTY = "doctorSpecialty"
        const val EXTRA_TIPO_ATENCION_ID = "tipoAtencionId"
        const val EXTRA_TIPO_ATENCION_NOMBRE = "tipoAtencionNombre"
        const val EXTRA_FECHA_HORA = "fechaHora"
        const val EXTRA_CONSULTORIO_ID = "idConsultorio"
        const val EXTRA_CONSULTORIO_NOMBRE = "consultorioNombre"
    }
}

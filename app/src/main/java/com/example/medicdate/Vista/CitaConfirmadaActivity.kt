package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Presentador.clsPresentadorCitaConfirmada
import com.example.medicdate.R
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class CitaConfirmadaActivity : AppCompatActivity(),
    View.OnClickListener {

    private lateinit var presenter: clsPresentadorCitaConfirmada
    private lateinit var btnViewAppointments: MaterialButton
    private lateinit var btnBackHome: MaterialButton
    private var idCita: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cita_confirmada)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idCita = intent.getIntExtra(EXTRA_ID_CITA, -1)
        val doctorName = intent.getStringExtra(EXTRA_DOCTOR_NAME).orEmpty()
        val doctorSpecialty = intent.getStringExtra(EXTRA_DOCTOR_SPECIALTY).orEmpty()
        val tipoAtencionNombre = intent.getStringExtra(EXTRA_TIPO_ATENCION_NOMBRE).orEmpty()
        val fechaHora = intent.getStringExtra(EXTRA_FECHA_HORA).orEmpty()
        val consultorioNombre = intent.getStringExtra(EXTRA_CONSULTORIO_NOMBRE).orEmpty()

        val nombreDoctorFinal = if (doctorName.isNotBlank()) doctorName else "Dra. Laura Méndez"
        val tipoFinal = if (tipoAtencionNombre.isNotBlank()) tipoAtencionNombre else "Consulta médica"

        val tvDetails = findViewById<TextView>(R.id.success_appointment_details)
        val tvDateLocation = findViewById<TextView>(R.id.success_appointment_date_location)

        tvDetails.text = if (doctorSpecialty.isNotBlank()) {
            "$doctorSpecialty · $nombreDoctorFinal"
        } else {
            "$tipoFinal · $nombreDoctorFinal"
        }

        if (fechaHora.isNotEmpty()) {
            val sufijo = if (consultorioNombre.isNotBlank()) {
                "$consultorioNombre · Cita #$idCita"
            } else {
                "Cita #$idCita"
            }
            tvDateLocation.text = "${formatearFechaCorta(fechaHora)} · $sufijo"
        } else {
            tvDateLocation.text = "Cita ID: #$idCita"
        }

        presenter = clsPresentadorCitaConfirmada(this)
        if (idCita != -1) {
            presenter.mostrarCita(idCita)
        }

        btnViewAppointments = findViewById(R.id.button_view_appointments)
        btnBackHome = findViewById(R.id.button_back_home)
        btnViewAppointments.setOnClickListener(this)
        btnBackHome.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_view_appointments,
            R.id.button_back_home -> volverAExplorar()
        }
    }

    private fun volverAExplorar() {
        val intentExplorar = Intent(this, ExplorarActivity::class.java)
        intentExplorar.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intentExplorar)
        finish()
    }

    fun mostrarDetalleId(id: Int) {
        // Actualizado mediante los extras de la cita confirmada
    }

    fun mostrarError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun formatearFechaCorta(fechaHoraIso: String): String {
        return try {
            val isoParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val date = isoParser.parse(fechaHoraIso) ?: return fechaHoraIso
            val formatter = SimpleDateFormat("EEE d MMM · HH:mm", Locale("es", "MX"))
            val formateado = formatter.format(date).replace(".", "")
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
        const val EXTRA_ID_CITA = "idCita"
        const val EXTRA_DOCTOR_NAME = "doctorName"
        const val EXTRA_DOCTOR_SPECIALTY = "doctorSpecialty"
        const val EXTRA_TIPO_ATENCION_NOMBRE = "tipoAtencionNombre"
        const val EXTRA_FECHA_HORA = "fechaHora"
        const val EXTRA_CONSULTORIO_NOMBRE = "consultorioNombre"
    }
}

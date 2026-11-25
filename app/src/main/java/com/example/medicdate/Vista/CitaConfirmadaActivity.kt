package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
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

class CitaConfirmadaActivity : AppCompatActivity() {
    private lateinit var presenter: clsPresentadorCitaConfirmada
    private var idCita: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cita_confirmada)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idCita = intent.getIntExtra("idCita", -1)
        val doctorName = intent.getStringExtra("doctorName") ?: "Dra. Laura Méndez"
        val tipoAtencionNombre = intent.getStringExtra("tipoAtencionNombre") ?: "Consulta médica"
        val fechaHora = intent.getStringExtra("fechaHora") ?: ""

        val tvDetails = findViewById<TextView>(R.id.success_appointment_details)
        val tvDateLocation = findViewById<TextView>(R.id.success_appointment_date_location)

        tvDetails.text = "$tipoAtencionNombre · $doctorName"
        if (fechaHora.isNotEmpty()) {
            tvDateLocation.text = "${formatearFechaCorta(fechaHora)} · Cita #$idCita"
        } else {
            tvDateLocation.text = "Cita ID: #$idCita"
        }

        presenter = clsPresentadorCitaConfirmada(this)
        if (idCita != -1) {
            presenter.mostrarCita(idCita)
        }

        findViewById<MaterialButton>(R.id.button_view_appointments).setOnClickListener {
            val intent = Intent(this, ExplorarActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        findViewById<MaterialButton>(R.id.button_back_home).setOnClickListener {
            val intent = Intent(this, ExplorarActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    fun mostrarDetalleId(id: Int) {
        // Updated via intent data or presenter
    }

    fun mostrarError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun formatearFechaCorta(fechaHoraIso: String): String {
        return try {
            val isoParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val date = isoParser.parse(fechaHoraIso) ?: return fechaHoraIso
            val formatter = SimpleDateFormat("EEE d MMM · HH:mm", Locale("es", "MX"))
            formatter.format(date).replace(".", "")
        } catch (_: Exception) {
            fechaHoraIso
        }
    }
}

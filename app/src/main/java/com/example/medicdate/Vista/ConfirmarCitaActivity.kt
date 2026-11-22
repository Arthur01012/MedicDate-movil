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
import com.example.medicdate.Modelo.ReservaRequest
import com.example.medicdate.Presentador.clsPresentadorConfirmarCita
import com.example.medicdate.R
import com.example.medicdate.Util.SessionManager
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class ConfirmarCitaActivity : AppCompatActivity() {
    private lateinit var presenter: clsPresentadorConfirmarCita
    private var doctorId: Int = -1
    private var tipoAtencionId: Int = -1
    private lateinit var fechaHora: String
    private var doctorName: String = ""
    private var tipoAtencionNombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmar_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        presenter = clsPresentadorConfirmarCita(this)
        doctorId = intent.getIntExtra("doctorId", -1)
        tipoAtencionId = intent.getIntExtra("tipoAtencionId", -1)
        fechaHora = intent.getStringExtra("fechaHora") ?: ""
        doctorName = intent.getStringExtra("doctorName") ?: "Doctor"
        tipoAtencionNombre = intent.getStringExtra("tipoAtencionNombre") ?: "Consulta médica"

        findViewById<View>(R.id.button_back).setOnClickListener { finish() }
        findViewById<View>(R.id.button_change_time).setOnClickListener { finish() }

        findViewById<TextView>(R.id.confirm_doctor_name).text = doctorName
        findViewById<TextView>(R.id.confirm_appointment_details).text = tipoAtencionNombre
        findViewById<TextView>(R.id.confirm_appointment_date_time).text = formatearFechaHoraBonita(fechaHora)

        findViewById<MaterialButton>(R.id.button_confirm_appointment).setOnClickListener { confirmar() }
    }

    private fun confirmar() {
        val usuarioId = if (SessionManager.usuarioId != -1) SessionManager.usuarioId else 1
        val request = ReservaRequest(
            id_usuario = usuarioId,
            id_empleado = if (doctorId != -1) doctorId else null,
            id_tipo_atencion = if (tipoAtencionId != -1) tipoAtencionId else 1,
            inicio = fechaHora
        )
        presenter.reservarCita(request)
    }

    fun mostrarError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun irAConfirmada(idCita: Int) {
        val i = Intent(this, CitaConfirmadaActivity::class.java)
        i.putExtra("idCita", idCita)
        startActivity(i)
        finish()
    }

    private fun formatearFechaHoraBonita(fechaHoraIso: String): String {
        return try {
            val isoParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val date = isoParser.parse(fechaHoraIso) ?: return fechaHoraIso
            val formatter = SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy · HH:mm", Locale("es", "MX"))
            formatter.format(date).replaceFirstChar { it.uppercase() }
        } catch (_: Exception) {
            fechaHoraIso
        }
    }
}

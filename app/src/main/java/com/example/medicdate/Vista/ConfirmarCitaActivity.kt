package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.R
import com.google.android.material.button.MaterialButton

class ConfirmarCitaActivity : AppCompatActivity() {
    private lateinit var appointmentDateLabel: String
    private lateinit var appointmentDateLong: String
    private lateinit var appointmentTime: String
    private lateinit var appointmentType: String
    private lateinit var appointmentDuration: String
    private lateinit var doctorName: String
    private lateinit var specialty: String
    private lateinit var location: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmar_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extras = intent.extras
        val requiredFields = listOf(
            CitaExtras.DATE_LABEL,
            CitaExtras.DATE_LONG,
            CitaExtras.TIME,
            CitaExtras.TYPE,
            CitaExtras.DURATION,
            CitaExtras.DOCTOR,
            CitaExtras.SPECIALTY,
            CitaExtras.LOCATION
        )
        if (extras == null || requiredFields.any { extras.getString(it).isNullOrBlank() }) {
            Toast.makeText(this, R.string.booking_data_missing, Toast.LENGTH_LONG).show()
            finish()
            return
        }

        appointmentDateLabel = requireNotNull(extras.getString(CitaExtras.DATE_LABEL))
        appointmentDateLong = requireNotNull(extras.getString(CitaExtras.DATE_LONG))
        appointmentTime = requireNotNull(extras.getString(CitaExtras.TIME))
        appointmentType = requireNotNull(extras.getString(CitaExtras.TYPE))
        appointmentDuration = requireNotNull(extras.getString(CitaExtras.DURATION))
        doctorName = requireNotNull(extras.getString(CitaExtras.DOCTOR))
        specialty = requireNotNull(extras.getString(CitaExtras.SPECIALTY))
        location = requireNotNull(extras.getString(CitaExtras.LOCATION))

        findViewById<TextView>(R.id.confirm_doctor_name).text = doctorName
        findViewById<TextView>(R.id.confirm_appointment_details).text = getString(
            R.string.confirmation_details_format,
            specialty,
            appointmentType,
            appointmentDuration
        )
        findViewById<TextView>(R.id.confirm_appointment_date_time).text = getString(
            R.string.confirmation_date_format,
            appointmentDateLong,
            appointmentTime
        )
        findViewById<TextView>(R.id.confirm_clinic_location).text = location

        findViewById<TextView>(R.id.button_back).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.button_change_time).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.button_choose_another_time).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.button_confirm_appointment).setOnClickListener {
            val confirmation = Intent(this, CitaConfirmadaActivity::class.java).apply {
                putExtra(CitaExtras.DATE_LABEL, appointmentDateLabel)
                putExtra(CitaExtras.DATE_LONG, appointmentDateLong)
                putExtra(CitaExtras.TIME, appointmentTime)
                putExtra(CitaExtras.TYPE, appointmentType)
                putExtra(CitaExtras.DURATION, appointmentDuration)
                putExtra(CitaExtras.DOCTOR, doctorName)
                putExtra(CitaExtras.SPECIALTY, specialty)
                putExtra(CitaExtras.LOCATION, location)
            }
            startActivity(confirmation)
            finish()
        }
    }
}

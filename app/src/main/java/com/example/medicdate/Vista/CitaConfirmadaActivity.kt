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

class CitaConfirmadaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cita_confirmada)
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

        val doctor = requireNotNull(extras.getString(CitaExtras.DOCTOR))
        val specialty = requireNotNull(extras.getString(CitaExtras.SPECIALTY))
        val dateLabel = requireNotNull(extras.getString(CitaExtras.DATE_LABEL))
        val dateLong = requireNotNull(extras.getString(CitaExtras.DATE_LONG))
        val time = requireNotNull(extras.getString(CitaExtras.TIME))
        val type = requireNotNull(extras.getString(CitaExtras.TYPE))
        val duration = requireNotNull(extras.getString(CitaExtras.DURATION))
        val location = requireNotNull(extras.getString(CitaExtras.LOCATION))

        findViewById<TextView>(R.id.success_appointment_details).text = "$specialty · $doctor"
        findViewById<TextView>(R.id.success_appointment_date_location).text = getString(
            R.string.success_appointment_format,
            dateLabel,
            time,
            location.substringBefore(" · ")
        )

        getSharedPreferences(CitaExtras.PREFERENCES, MODE_PRIVATE).edit()
            .putString(CitaExtras.DATE_LABEL, dateLabel)
            .putString(CitaExtras.DATE_LONG, dateLong)
            .putString(CitaExtras.TIME, time)
            .putString(CitaExtras.TYPE, type)
            .putString(CitaExtras.DURATION, duration)
            .putString(CitaExtras.DOCTOR, doctor)
            .putString(CitaExtras.SPECIALTY, specialty)
            .putString(CitaExtras.LOCATION, location)
            .apply()

        val homeIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        findViewById<MaterialButton>(R.id.button_view_appointments).setOnClickListener {
            startActivity(homeIntent)
            finish()
        }
        findViewById<MaterialButton>(R.id.button_back_home).setOnClickListener {
            startActivity(homeIntent)
            finish()
        }
    }
}

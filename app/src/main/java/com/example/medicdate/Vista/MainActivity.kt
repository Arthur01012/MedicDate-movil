package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<MaterialButton>(R.id.button_book_appointment).setOnClickListener {
            startActivity(Intent(this, RecervarCitaActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val preferences = getSharedPreferences(CitaExtras.PREFERENCES, MODE_PRIVATE)
        val date = preferences.getString(CitaExtras.DATE_LABEL, null)
        val time = preferences.getString(CitaExtras.TIME, null)
        val doctor = preferences.getString(CitaExtras.DOCTOR, null)
        val specialty = preferences.getString(CitaExtras.SPECIALTY, null)
        val location = preferences.getString(CitaExtras.LOCATION, null)
        val card = findViewById<MaterialCardView>(R.id.last_appointment_card)
        val hasAppointment = listOf(date, time, doctor, specialty, location).all { !it.isNullOrBlank() }

        card.visibility = if (hasAppointment) View.VISIBLE else View.GONE
        if (hasAppointment) {
            findViewById<TextView>(R.id.last_appointment_doctor).text = "$specialty · $doctor"
            findViewById<TextView>(R.id.last_appointment_summary).text = getString(
                R.string.success_appointment_format,
                date,
                time,
                location?.substringBefore(" ·").orEmpty()
            )
            findViewById<MaterialButton>(R.id.button_book_appointment)
                .setText(R.string.book_another_appointment)
        } else {
            findViewById<MaterialButton>(R.id.button_book_appointment)
                .setText(R.string.book_appointment)
        }
    }
}

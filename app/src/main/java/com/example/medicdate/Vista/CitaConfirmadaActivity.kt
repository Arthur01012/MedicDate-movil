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

        presenter = clsPresentadorCitaConfirmada(this)
        idCita = intent.getIntExtra("idCita", -1)
        if (idCita != -1) {
            presenter.mostrarCita(idCita)
        } else {
            mostrarError("ID de cita no recibido")
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
        findViewById<TextView>(R.id.success_appointment_details).text = "Cita confirmada. ID: #$id"
    }

    fun mostrarError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}

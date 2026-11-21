package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Presentador.clsPresentadorConfirmarCita
import com.example.medicdate.R
import com.google.android.material.button.MaterialButton

class ConfirmarCitaActivity : AppCompatActivity() {
    private lateinit var presenter: clsPresentadorConfirmarCita
    private var doctorId = -1
    private var tipoAtencionId = -1
    private lateinit var fechaHora: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmar_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

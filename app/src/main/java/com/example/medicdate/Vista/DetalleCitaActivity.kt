package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Presentador.clsPresentadorDetalleCita
import com.example.medicdate.R

class DetalleCitaActivity : AppCompatActivity() {
    private lateinit var presenter: clsPresentadorDetalleCita
    private lateinit var btnVolver: ImageView
    private lateinit var txtEstado: TextView
    private lateinit var txtFecha: TextView
    private lateinit var txtHorario: TextView
    private lateinit var txtMedico: TextView
    private lateinit var txtAtencion: TextView
    private lateinit var txtLugar: TextView
    private lateinit var btnCancelar: Button
    private lateinit var txtAviso: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVolver = findViewById(R.id.btnVolver)
        txtEstado = findViewById(R.id.txtEstado)
        txtFecha = findViewById(R.id.txtFecha)
        txtHorario = findViewById(R.id.txtHorario)
        txtMedico = findViewById(R.id.txtMedico)
        txtAtencion = findViewById(R.id.txtAtencion)
        txtLugar = findViewById(R.id.txtLugar)
        btnCancelar = findViewById(R.id.btnCancelar)
        txtAviso = findViewById(R.id.txtAviso)

        presenter = clsPresentadorDetalleCita(this)

        configurarListeners()
    }

    private fun configurarListeners()
    {
        btnVolver.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                finish()
            }
        })
        btnCancelar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                presenter.cancelarCita()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        presenter.cargarDetalle(intent.getIntExtra("ID_CITA", 0), intent.getIntExtra("ID_USUARIO", 0))
    }

    fun cancelacionExitosa(mensaje: String)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        finish()
    }

    fun mostrarDetalle(estado: String, fecha: String, horario: String, medico: String,
                       atencion: String, lugar: String, sePuedeCancelar: Boolean)
    {
        txtEstado.text = estado
        txtFecha.text = fecha
        txtHorario.text = horario
        txtMedico.text = medico
        txtAtencion.text = atencion
        txtLugar.text = lugar
        btnCancelar.isEnabled = sePuedeCancelar
        txtAviso.text = if (sePuedeCancelar) "Disponible hasta un día antes de la cita."
            else "Ya no puedes cancelar esta cita porque faltan menos de 24 horas."
    }

    fun irALogin()
    {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun mostrarError(mensaje: String)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}

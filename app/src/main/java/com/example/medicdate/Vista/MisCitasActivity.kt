package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicdate.Adaptador.CitasAdaptador
import com.example.medicdate.Modelo.dataCitaPaciente
import com.example.medicdate.Presentador.clsPresentadorMisCitas
import com.example.medicdate.R

class MisCitasActivity : AppCompatActivity() {
    private lateinit var presenter: clsPresentadorMisCitas
    private lateinit var txtProximas: TextView
    private lateinit var txtHistorial: TextView
    private lateinit var rvCitas: RecyclerView
    private lateinit var txtVacio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mis_citas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtProximas = findViewById(R.id.txtProximas)
        txtHistorial = findViewById(R.id.txtHistorial)
        rvCitas = findViewById(R.id.rvCitas)
        txtVacio = findViewById(R.id.txtVacio)
        rvCitas.layoutManager = LinearLayoutManager(this)

        presenter = clsPresentadorMisCitas(this)

        configurarListeners()
    }

    private fun configurarListeners()
    {
        txtProximas.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                presenter.mostrarProximas()
            }
        })
        txtHistorial.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                presenter.mostrarHistorial()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        presenter.cargarCitas(intent.getIntExtra("ID_USUARIO", 0))
    }

    fun marcarPestana(proximas: Boolean)
    {
        val azul = ContextCompat.getColor(this, R.color.azul)
        val gris = ContextCompat.getColor(this, R.color.texto_secundario)
        txtProximas.setBackgroundResource(if (proximas) R.drawable.bg_input else R.drawable.bg_card)
        txtHistorial.setBackgroundResource(if (proximas) R.drawable.bg_card else R.drawable.bg_input)
        txtProximas.setTextColor(if (proximas) azul else gris)
        txtHistorial.setTextColor(if (proximas) gris else azul)
    }

    fun mostrarCitas(lista: List<dataCitaPaciente>)
    {
        txtVacio.visibility = View.GONE
        rvCitas.visibility = View.VISIBLE
        rvCitas.adapter = CitasAdaptador(this, lista)
    }

    fun mostrarVacio(mensaje: String)
    {
        rvCitas.visibility = View.GONE
        txtVacio.text = mensaje
        txtVacio.visibility = View.VISIBLE
    }

    fun verDetalleCita(cita: dataCitaPaciente)
    {
        presenter.verDetalleCita(cita)
    }

    fun abrirDetalleCita(idCita: Int, idUsuario: Int)
    {
        val intent = Intent(this, DetalleCitaActivity::class.java)
        intent.putExtra("ID_CITA", idCita)
        intent.putExtra("ID_USUARIO", idUsuario)
        startActivity(intent)
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

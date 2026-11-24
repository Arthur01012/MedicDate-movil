package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Modelo.dataPregunta
import com.example.medicdate.Presentador.clsPresentadorRegistro
import com.example.medicdate.R
import com.example.medicdate.Util.SessionManager

class RegistroActivity : AppCompatActivity() {
    private lateinit var presenter: clsPresentadorRegistro
    private lateinit var txtNombres: EditText
    private lateinit var txtApellidoPaterno: EditText
    private lateinit var txtApellidoMaterno: EditText
    private lateinit var txtCorreo: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtClave: EditText
    private lateinit var spPregunta: Spinner
    private lateinit var txtRespuesta: EditText
    private lateinit var btnCrearCuenta: Button
    private lateinit var btnVolver: ImageView
    private lateinit var txtIniciarSesion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtNombres = findViewById(R.id.txtNombres)
        txtApellidoPaterno = findViewById(R.id.txtApellidoPaterno)
        txtApellidoMaterno = findViewById(R.id.txtApellidoMaterno)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtTelefono = findViewById(R.id.txtTelefono)
        txtClave = findViewById(R.id.txtClave)
        spPregunta = findViewById(R.id.spPregunta)
        txtRespuesta = findViewById(R.id.txtRespuesta)
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)
        btnVolver = findViewById(R.id.btnVolver)
        txtIniciarSesion = findViewById(R.id.txtIniciarSesion)

        presenter = clsPresentadorRegistro(this)

        configurarListeners()
        presenter.cargarPreguntas()
    }

    private fun configurarListeners()
    {
        btnCrearCuenta.setOnClickListener(this::ejecutarRegistro)
        btnVolver.setOnClickListener(this::ejecutarVolver)
        txtIniciarSesion.setOnClickListener(this::ejecutarVolver)
    }

    fun ejecutarRegistro(v: View)
    {
        presenter.crearCuenta(
            txtNombres.text.toString().trim(),
            txtApellidoPaterno.text.toString().trim(),
            txtApellidoMaterno.text.toString().trim(),
            txtCorreo.text.toString().trim(),
            txtTelefono.text.toString().trim(),
            txtClave.text.toString().trim(),
            spPregunta.selectedItemPosition,
            txtRespuesta.text.toString().trim()
        )
    }

    fun ejecutarVolver(v: View)
    {
        finish()
    }

    fun mostrarPreguntas(lista: List<dataPregunta>)
    {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPregunta.adapter = adapter
    }

    fun registroExitoso(mensaje: String, idUsuario: Int, idPaciente: Int)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        SessionManager.setSession(this, idUsuario, idPaciente)
        val intent = Intent(this, ExplorarActivity::class.java)
        intent.putExtra("ID_USUARIO", idUsuario)
        intent.putExtra("ID_PACIENTE", idPaciente)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun mostrarError(mensaje: String)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Presentador.clsPresentadorRecuperarContrasena
import com.example.medicdate.R

class RecuperarContrasenaActivity : AppCompatActivity() {
    private lateinit var btnVolver: ImageView
    private lateinit var txtPregunta: TextView
    private lateinit var txtRespuesta: EditText
    private lateinit var txtClaveNueva: EditText
    private lateinit var txtConfirmarClave: EditText
    private lateinit var btnCambiar: Button
    private lateinit var presenter: clsPresentadorRecuperarContrasena

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contrasena)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVolver = findViewById(R.id.btnVolver)
        txtPregunta = findViewById(R.id.txtPregunta)
        txtRespuesta = findViewById(R.id.txtRespuesta)
        txtClaveNueva = findViewById(R.id.txtClaveNueva)
        txtConfirmarClave = findViewById(R.id.txtConfirmarClave)
        btnCambiar = findViewById(R.id.btnCambiar)

        // La pregunta llega desde la pantalla anterior
        txtPregunta.text = intent.getStringExtra("PREGUNTA")

        presenter = clsPresentadorRecuperarContrasena(this)

        btnVolver.setOnClickListener { finish() }
        btnCambiar.setOnClickListener(this::ejecutarCambiar)
    }

    fun ejecutarCambiar(v: View)
    {
        presenter.cambiarContrasena(
            intent.getStringExtra("CORREO") ?: "",
            txtRespuesta.text.toString().trim(),
            txtClaveNueva.text.toString(),
            txtConfirmarClave.text.toString()
        )
    }

    fun contrasenaCambiada(mensaje: String)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        irALogin()
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

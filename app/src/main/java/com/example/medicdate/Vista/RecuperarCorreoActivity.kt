package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Presentador.clsPresentadorRecuperarCorreo
import com.example.medicdate.R

class RecuperarCorreoActivity : AppCompatActivity() {
    private lateinit var btnVolver: ImageView
    private lateinit var txtCorreo: EditText
    private lateinit var btnContinuar: Button
    private lateinit var presenter: clsPresentadorRecuperarCorreo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_correo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVolver = findViewById(R.id.btnVolver)
        txtCorreo = findViewById(R.id.txtCorreo)
        btnContinuar = findViewById(R.id.btnContinuar)

        presenter = clsPresentadorRecuperarCorreo(this)

        btnVolver.setOnClickListener { finish() }
        btnContinuar.setOnClickListener(this::ejecutarContinuar)
    }

    fun ejecutarContinuar(v: View)
    {
        presenter.buscarPregunta(txtCorreo.text.toString().trim())
    }

    fun irAResponderPregunta(correo: String, pregunta: String)
    {
        val intent = Intent(this, RecuperarContrasenaActivity::class.java)
        intent.putExtra("CORREO", correo)
        intent.putExtra("PREGUNTA", pregunta)
        startActivity(intent)
    }

    fun mostrarError(mensaje: String)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}

package com.example.medicdate.Vista

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.R

class DatosPersonalesActivity : AppCompatActivity() {
    private lateinit var txtVolver: TextView
    private lateinit var txtNombres: EditText
    private lateinit var txtApellidoPaterno: EditText
    private lateinit var txtApellidoMaterno: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtCorreo: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos_personales)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtVolver = findViewById(R.id.txtVolver)
        txtNombres = findViewById(R.id.txtNombres)
        txtApellidoPaterno = findViewById(R.id.txtApellidoPaterno)
        txtApellidoMaterno = findViewById(R.id.txtApellidoMaterno)
        txtTelefono = findViewById(R.id.txtTelefono)
        txtCorreo = findViewById(R.id.txtCorreo)
        btnGuardar = findViewById(R.id.btnGuardar)

        txtVolver.setOnClickListener { finish() }
    }

    fun mostrarDatos(nombres: String, paterno: String, materno: String, telefono: String, correo: String)
    {
        txtNombres.setText(nombres)
        txtApellidoPaterno.setText(paterno)
        txtApellidoMaterno.setText(materno)
        txtTelefono.setText(telefono)
        txtCorreo.setText(correo)
    }

    fun mostrarError(mensaje: String)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}

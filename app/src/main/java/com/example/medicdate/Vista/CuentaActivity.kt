package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Presentador.clsPresentadorCuenta
import com.example.medicdate.R

class CuentaActivity : AppCompatActivity() {
    private lateinit var presenter: clsPresentadorCuenta
    private lateinit var txtNombre: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var txtDatosPersonales: TextView
    private lateinit var btnCerrarSesion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cuenta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtDatosPersonales = findViewById(R.id.txtDatosPersonales)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)

        presenter = clsPresentadorCuenta(this)

        btnCerrarSesion.setOnClickListener(this::ejecutarCerrarSesion)
        txtDatosPersonales.setOnClickListener(this::irADatosPersonales)
        presenter.cargarCuenta(intent.getIntExtra("ID_USUARIO", 0))
    }

    fun ejecutarCerrarSesion(v: View)
    {
        irALogin()
    }

    fun irADatosPersonales(v: View)
    {
        val intent = Intent(this, DatosPersonalesActivity::class.java)
        intent.putExtra("ID_USUARIO", this.intent.getIntExtra("ID_USUARIO", 0))
        startActivity(intent)
    }

    fun mostrarCuenta(nombre: String, correo: String)
    {
        txtNombre.text = nombre
        txtCorreo.text = correo
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

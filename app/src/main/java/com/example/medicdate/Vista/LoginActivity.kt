package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Presentador.clsPresentadorLogin
import com.example.medicdate.R
import com.example.medicdate.Util.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var presenter: clsPresentadorLogin
    private lateinit var txtCorreo: EditText
    private lateinit var txtClave: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtCrearCuenta: TextView
    private lateinit var txtOlvidoClave: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        SessionManager.init(this)

        txtCorreo = findViewById(R.id.txtCorreo)
        txtClave = findViewById(R.id.txtClave)
        btnLogin = findViewById(R.id.btnLogin)
        txtCrearCuenta = findViewById(R.id.txtCrearCuenta)
        txtOlvidoClave = findViewById(R.id.txtOlvidoClave)

        presenter = clsPresentadorLogin(this)

        btnLogin.setOnClickListener(this::ejecutarInicioSesion)
        txtCrearCuenta.setOnClickListener(this::ejecutarCrearCuenta)
        txtOlvidoClave.setOnClickListener(this::ejecutarOlvidoClave)
    }

    fun ejecutarInicioSesion(v: View)
    {
        presenter.realizarLogin(txtCorreo.text.toString().trim(), txtClave.text.toString().trim())
    }

    fun ejecutarOlvidoClave(v: View)
    {
        startActivity(Intent(this, RecuperarCorreoActivity::class.java))
    }

    fun ejecutarCrearCuenta(v: View)
    {
        startActivity(Intent(this, RegistroActivity::class.java))
    }

    fun loginExitoso(idUsuario: Int, idPaciente: Int)
    {
        SessionManager.setSession(this, idUsuario, idPaciente)
        val intent = Intent(this, ExplorarActivity::class.java)
        intent.putExtra("ID_USUARIO", idUsuario)
        intent.putExtra("ID_PACIENTE", idPaciente)
        startActivity(intent)
        finish()
    }

    fun mostrarError(mensaje: String)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}

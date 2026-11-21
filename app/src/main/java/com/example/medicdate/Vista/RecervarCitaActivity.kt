package com.example.medicdate.Vista

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.Presentador.clsPresentadorReservaCita
import com.example.medicdate.R


class RecervarCitaActivity : AppCompatActivity() {

    private lateinit var presenter: clsPresentadorReservaCita
    private var doctorId: Int = -1
    private var tipoAtencionId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recervar_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        presenter = clsPresentadorReservaCita(this)
        doctorId = intent.getIntExtra("doctorId", -1)
        tipoAtencionId = intent.getIntExtra("tipoAtencionId", -1)
    }
    fun mostrarHorarios(horarios: List<Horario>) {
        // populate RecyclerView adapter
    }
    fun mostrarError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
    fun irAConfirmarCita(fechaHora: String) {
        val i = Intent(this, ConfirmarCitaActivity::class.java)
        i.putExtra("doctorId", doctorId)
        i.putExtra("tipoAtencionId", tipoAtencionId)
        i.putExtra("fechaHora", fechaHora)
        startActivity(i)
    }
}

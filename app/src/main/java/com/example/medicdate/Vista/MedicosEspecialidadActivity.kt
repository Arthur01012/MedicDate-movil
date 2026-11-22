package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicdate.Adaptador.MedicoEspecialidadAdapter
import com.example.medicdate.Modelo.MedicoEspecialidad
import com.example.medicdate.Presentador.clsPresentadorMedicosEspecialidad
import com.example.medicdate.R
import com.google.android.material.appbar.MaterialToolbar

class MedicosEspecialidadActivity : AppCompatActivity() {

    private lateinit var presenter: clsPresentadorMedicosEspecialidad
    private lateinit var toolbar: MaterialToolbar
    private lateinit var txtEspecialidad: TextView
    private lateinit var rvMedicosEspecialidad: RecyclerView
    private lateinit var adapter: MedicoEspecialidadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medicos_especialidad)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = findViewById(R.id.toolbar)
        txtEspecialidad = findViewById(R.id.txtEspecialidad)
        rvMedicosEspecialidad = findViewById(R.id.rvMedicosEspecialidad)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val idEspecialidad = intent.getIntExtra("ID_ESPECIALIDAD", 0)
        val nombreEspecialidad = intent.getStringExtra("NOMBRE_ESPECIALIDAD").orEmpty()

        adapter = MedicoEspecialidadAdapter(emptyList(), "") { medico ->
            val intent = Intent(this, RecervarCitaActivity::class.java)
            intent.putExtra("doctorId", medico.idEmpleado)
            intent.putExtra("doctorName", medico.nombre)
            intent.putExtra("doctorSpecialty", nombreEspecialidad)
            intent.putExtra("tipoAtencionId", if (idEspecialidad != 0) idEspecialidad else 1)
            startActivity(intent)
        }

        rvMedicosEspecialidad.layoutManager = LinearLayoutManager(this)
        rvMedicosEspecialidad.adapter = adapter

        presenter = clsPresentadorMedicosEspecialidad(this)
        presenter.cargarMedicos(idEspecialidad, nombreEspecialidad)
    }

    fun mostrarTituloEspecialidad(nombre: String) {
        txtEspecialidad.text = nombre
    }

    fun mostrarMedicos(medicos: List<MedicoEspecialidad>, nombreEspecialidad: String) {
        adapter.actualizarDatos(medicos, nombreEspecialidad)
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
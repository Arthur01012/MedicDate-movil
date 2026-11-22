package com.example.medicdate.Vista

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicdate.Adaptador.EspecialidadAdapter
import com.example.medicdate.Adaptador.MedicoExplorarAdapter
import com.example.medicdate.Modelo.Especialidad
import com.example.medicdate.Modelo.MedicoExplorar
import com.example.medicdate.Modelo.ProximaCita
import com.example.medicdate.Presentador.clsPresentadorExplorar
import com.example.medicdate.R
import java.text.SimpleDateFormat
import java.util.Locale

class ExplorarActivity : AppCompatActivity() {

    private lateinit var presenter: clsPresentadorExplorar

    private lateinit var txtSaludo: TextView
    private lateinit var edtBuscar: EditText
    private lateinit var txtFechaCita: TextView
    private lateinit var txtDetalleCita: TextView
    private lateinit var rvEspecialidades: RecyclerView
    private lateinit var rvMedicos: RecyclerView

    private lateinit var especialidadAdapter: EspecialidadAdapter
    private lateinit var medicoAdapter: MedicoExplorarAdapter

    private var idPaciente: Int = 1
    private var idUsuario: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_explorar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idPaciente = intent.getIntExtra("ID_PACIENTE", 1)
        idUsuario = intent.getIntExtra("ID_USUARIO", 0)

        inicializarVistas()
        configurarListas()
        configurarEventos()

        presenter = clsPresentadorExplorar(this)
        presenter.cargarExplorar(idPaciente)
    }

    private fun inicializarVistas() {
        txtSaludo = findViewById(R.id.txtSaludo)
        edtBuscar = findViewById(R.id.edtBuscar)
        txtFechaCita = findViewById(R.id.txtFechaCita)
        txtDetalleCita = findViewById(R.id.txtDetalleCita)
        rvEspecialidades = findViewById(R.id.rvEspecialidades)
        rvMedicos = findViewById(R.id.rvMedicos)
    }

    private fun configurarListas() {
        especialidadAdapter = EspecialidadAdapter(emptyList()) { especialidad ->
            val intent = Intent(this, MedicosEspecialidadActivity::class.java)
            intent.putExtra("ID_ESPECIALIDAD", especialidad.idEspecialidad)
            intent.putExtra("NOMBRE_ESPECIALIDAD", especialidad.nombre)
            startActivity(intent)
        }
        rvEspecialidades.layoutManager = GridLayoutManager(this, 2)
        rvEspecialidades.adapter = especialidadAdapter

        medicoAdapter = MedicoExplorarAdapter(emptyList()) { medico ->
            val intent = Intent(this, RecervarCitaActivity::class.java)
            intent.putExtra("doctorId", medico.idEmpleado)
            intent.putExtra("doctorName", medico.nombre)
            intent.putExtra("doctorSpecialty", medico.especialidades)
            intent.putExtra("tipoAtencionId", 1)
            startActivity(intent)
        }
        rvMedicos.layoutManager = LinearLayoutManager(this)
        rvMedicos.adapter = medicoAdapter
    }

    private fun configurarEventos() {
        edtBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.filtrarBusqueda(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        // Barra inferior de tu layout activity_explorar.xml (3 TextView dentro del segundo LinearLayout)
        val contenedorPrincipal = findViewById<LinearLayout>(R.id.main)
        val barraInferior = contenedorPrincipal.getChildAt(1) as? LinearLayout
        if (barraInferior != null && barraInferior.childCount >= 3) {
            val tvExplorar = barraInferior.getChildAt(0) as TextView
            val tvCitas = barraInferior.getChildAt(1) as TextView
            val tvCuenta = barraInferior.getChildAt(2) as TextView

            tvExplorar.setOnClickListener {
                presenter.cargarExplorar(idPaciente)
            }

            tvCitas.setOnClickListener {
                Toast.makeText(this, "Pantalla de Citas pendiente", Toast.LENGTH_SHORT).show()
            }

            tvCuenta.setOnClickListener {
                val intent = Intent(this, CuentaActivity::class.java)
                intent.putExtra("ID_USUARIO", if (idUsuario != 0) idUsuario else idPaciente)
                startActivity(intent)
            }
        }
    }

    fun mostrarSaludo(textoSaludo: String) {
        txtSaludo.text = textoSaludo
    }

    fun mostrarProximaCita(proximaCita: ProximaCita?) {
        if (proximaCita == null) {
            txtFechaCita.text = "Sin citas próximas"
            txtDetalleCita.text = ""
            return
        }

        val fechaBonita = formatearFechaHora(proximaCita.inicio)
        val consultorio = if (!proximaCita.consultorio.isNullOrBlank()) {
            " · ${proximaCita.consultorio}"
        } else {
            ""
        }

        txtFechaCita.text = "$fechaBonita$consultorio"
        txtDetalleCita.text = "${proximaCita.especialidad} · ${proximaCita.medico}"
    }

    fun mostrarEspecialidades(especialidades: List<Especialidad>) {
        especialidadAdapter.actualizarDatos(especialidades)
    }

    fun mostrarMedicos(medicos: List<MedicoExplorar>) {
        medicoAdapter.actualizarDatos(medicos)
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun formatearFechaHora(fechaHoraSql: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val fecha = parser.parse(fechaHoraSql) ?: return fechaHoraSql
            val formateador = SimpleDateFormat("EEE d MMM · HH:mm", Locale("es", "MX"))
            formateador.format(fecha).replace(".", "").replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale("es", "MX")) else it.toString()
            }
        } catch (_: Exception) {
            fechaHoraSql
        }
    }
}
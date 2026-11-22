package com.example.medicdate.Util

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "medicdate_session_prefs"
    private const val KEY_USUARIO_ID = "usuario_id"
    private const val KEY_PACIENTE_ID = "paciente_id"
    private const val KEY_TOKEN = "token"

    private var prefs: SharedPreferences? = null
    private var inMemoryUsuarioId: Int = -1
    private var inMemoryPacienteId: Int = -1

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            inMemoryUsuarioId = prefs?.getInt(KEY_USUARIO_ID, -1) ?: -1
            inMemoryPacienteId = prefs?.getInt(KEY_PACIENTE_ID, -1) ?: -1
        }
    }

    var usuarioId: Int
        get() = prefs?.getInt(KEY_USUARIO_ID, inMemoryUsuarioId) ?: inMemoryUsuarioId
        set(value) {
            inMemoryUsuarioId = value
            prefs?.edit()?.putInt(KEY_USUARIO_ID, value)?.apply()
        }

    var pacienteId: Int
        get() = prefs?.getInt(KEY_PACIENTE_ID, inMemoryPacienteId) ?: inMemoryPacienteId
        set(value) {
            inMemoryPacienteId = value
            prefs?.edit()?.putInt(KEY_PACIENTE_ID, value)?.apply()
        }

    var token: String?
        get() = prefs?.getString(KEY_TOKEN, null)
        set(value) {
            prefs?.edit()?.putString(KEY_TOKEN, value)?.apply()
        }

    fun setSession(context: Context, idUsuario: Int, idPaciente: Int, authToken: String? = null) {
        init(context)
        usuarioId = idUsuario
        pacienteId = idPaciente
        token = authToken
    }

    fun setSession(idUsuario: Int, authToken: String?) {
        usuarioId = idUsuario
        token = authToken
    }

    fun clear(context: Context) {
        init(context)
        prefs?.edit()?.clear()?.apply()
        inMemoryUsuarioId = -1
        inMemoryPacienteId = -1
        token = null
    }
}

package com.example.medicdate.Util

object SessionManager {
    var usuarioId: Int = -1
    var token: String? = null
    // Call this after a successful login
    fun setSession(id: Int, authToken: String?) {
        usuarioId = id
        token = authToken
    }
    fun clear() {
        usuarioId = -1
        token = null
    }
}

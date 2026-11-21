package com.example.medicdate.InterfaceApi

import com.example.medicdate.Modelo.LoginRequest
import com.example.medicdate.Modelo.dataCuenta
import com.example.medicdate.Modelo.dataLogin
import com.example.medicdate.Modelo.dataRegistro
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import com.example.medicdate.Modelo.dataExplorar
import com.example.medicdate.Modelo.dataMedicosEspecialidad

interface ApiService {
    @POST("reservar_cita.php")
    suspend fun reservarCita(@Body request: com.example.medicdate.Modelo.ReservaRequest): com.example.medicdate.Modelo.ReservaResponse
    @POST("Disponibilidad.php")
    suspend fun obtenerDisponibilidad(@Body request: com.example.medicdate.Modelo.DisponibilidadRequest): com.example.medicdate.Modelo.DisponibilidadResponse
    @POST("reservar_cita.php")
    suspend fun reservarCita(@Body request: com.example.medicdate.Modelo.ReservaRequest): com.example.medicdate.Modelo.ReservaResponse
    @POST("login.php")
    suspend fun iniciarSesion(@Body request: LoginRequest): dataLogin

    @FormUrlEncoded
    @POST("registro.php")
    suspend fun registrarUsuario(
        @Field("nombres") nombres: String,
        @Field("apellidoPaterno") apellidoPaterno: String,
        @Field("apellidoMaterno") apellidoMaterno: String,
        @Field("correo") correo: String,
        @Field("telefono") telefono: String,
        @Field("clave") clave: String
    ): dataRegistro

    @GET("obtener_cuenta.php")
    suspend fun obtenerCuenta(@Query("idUsuario") idUsuario: Int): dataCuenta

    @GET("explorar.php")
    suspend fun obtenerExplorar(
        @Query("idPaciente") idPaciente: Int
    ): dataExplorar

    @GET("medicos_especialidad.php")
    suspend fun obtenerMedicosEspecialidad(
        @Query("idEspecialidad") idEspecialidad: Int
    ): dataMedicosEspecialidad
}

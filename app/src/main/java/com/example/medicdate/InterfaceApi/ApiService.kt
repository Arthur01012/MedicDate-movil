package com.example.medicdate.InterfaceApi

import com.example.medicdate.Modelo.CitaPaciente
import com.example.medicdate.Modelo.DetalleCita
import com.example.medicdate.Modelo.DisponibilidadRequest
import com.example.medicdate.Modelo.DisponibilidadResponse
import com.example.medicdate.Modelo.LoginRequest
import com.example.medicdate.Modelo.ReservaRequest
import com.example.medicdate.Modelo.ReservaResponse
import com.example.medicdate.Modelo.dataCuenta
import com.example.medicdate.Modelo.dataDatosPersonales
import com.example.medicdate.Modelo.dataExplorar
import com.example.medicdate.Modelo.dataLogin
import com.example.medicdate.Modelo.dataMedicosEspecialidad
import com.example.medicdate.Modelo.dataRegistro
import com.example.medicdate.Modelo.DatosPersonalesRequest
import com.example.medicdate.Modelo.dataGuardarDatosPersonales
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("reservar_cita.php")
    suspend fun reservarCita(@Body request: ReservaRequest): ReservaResponse

    @POST("disponibilidad.php")
    suspend fun obtenerDisponibilidad(@Body request: DisponibilidadRequest): DisponibilidadResponse

    @GET("disponibilidad.php")
    suspend fun obtenerDisponibilidadGet(@Query("datos") datosJson: String): DisponibilidadResponse

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

    @POST("actualizar_datos_personales.php")
    suspend fun actualizarDatosPersonales(@Body request: DatosPersonalesRequest): dataGuardarDatosPersonales

    @GET("explorar.php")
    suspend fun obtenerExplorar(
        @Query("idPaciente") idPaciente: Int
    ): dataExplorar

    @GET("medicos_especialidad.php")
    suspend fun obtenerMedicosEspecialidad(
        @Query("idEspecialidad") idEspecialidad: Int
    ): dataMedicosEspecialidad

    @GET("citas_paciente.php")
    suspend fun obtenerCitasPaciente(
        @Query("id_usuario") idUsuario: Int
    ): List<CitaPaciente>

    @GET("detalle_cita.php")
    suspend fun obtenerDetalleCita(
        @Query("id_cita") idCita: Int,
        @Query("id_usuario") idUsuario: Int
    ): DetalleCita

    @GET("datos_personales.php")
    suspend fun obtenerDatosPersonales(@Query("idUsuario") idUsuario: Int, @Query("t") t: Long): dataDatosPersonales
}

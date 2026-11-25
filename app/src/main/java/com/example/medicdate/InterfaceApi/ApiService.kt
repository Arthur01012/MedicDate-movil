package com.example.medicdate.InterfaceApi

import com.example.medicdate.Modelo.CitaPaciente
import com.example.medicdate.Modelo.DetalleCita
import com.example.medicdate.Modelo.DisponibilidadRequest
import com.example.medicdate.Modelo.DisponibilidadResponse
import com.example.medicdate.Modelo.LoginRequest
import com.example.medicdate.Modelo.dataCitaPaciente
import com.example.medicdate.Modelo.dataDetalleCita
import com.example.medicdate.Modelo.dataCancelarCita
import com.example.medicdate.Modelo.ReservaRequest
import com.example.medicdate.Modelo.ReservaResponse
import com.example.medicdate.Modelo.CuentaResponse
import com.example.medicdate.Modelo.DatosPersonalesResponse
import com.example.medicdate.Modelo.dataExplorar
import com.example.medicdate.Modelo.LoginResponse
import com.example.medicdate.Modelo.CorreoRecuperarRequest
import com.example.medicdate.Modelo.RecuperarRequest
import com.example.medicdate.Modelo.PreguntaRecuperacionResponse
import com.example.medicdate.Modelo.RecuperarResponse
import com.example.medicdate.Modelo.dataMedicosEspecialidad
import com.example.medicdate.Modelo.RegistroResponse
import com.example.medicdate.Modelo.DatosPersonalesRequest
import com.example.medicdate.Modelo.GuardarDatosPersonalesResponse
import com.example.medicdate.Modelo.PreguntasResponse
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
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse

    @FormUrlEncoded
    @POST("registro.php")
    suspend fun registrarUsuario(
        @Field("nombres") nombres: String,
        @Field("apellidoPaterno") apellidoPaterno: String,
        @Field("apellidoMaterno") apellidoMaterno: String,
        @Field("correo") correo: String,
        @Field("telefono") telefono: String,
        @Field("clave") clave: String,
        @Field("idPregunta") idPregunta: Int,
        @Field("respuesta") respuesta: String
    ): RegistroResponse

    @POST("pregunta_recuperacion.php")
    suspend fun obtenerPreguntaRecuperacion(@Body request: CorreoRecuperarRequest): PreguntaRecuperacionResponse

    @POST("recuperar_contrasena.php")
    suspend fun recuperarContrasena(@Body request: RecuperarRequest): RecuperarResponse

    @GET("obtener_cuenta.php")
    suspend fun obtenerCuenta(@Query("idUsuario") idUsuario: Int): CuentaResponse

    @POST("actualizar_datos_personales.php")
    suspend fun actualizarDatosPersonales(@Body request: DatosPersonalesRequest): GuardarDatosPersonalesResponse

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
    ): List<dataCitaPaciente>

    @GET("detalle_cita.php")
    suspend fun obtenerDetalleCita(
        @Query("id_cita") idCita: Int,
        @Query("id_usuario") idUsuario: Int
    ): dataDetalleCita

    @FormUrlEncoded
    @POST("cancelar_cita.php")
    suspend fun cancelarCita(
        @Field("id_cita") idCita: Int,
        @Field("id_usuario") idUsuario: Int
    ): dataCancelarCita

    @GET("obtener_contrasena.php")
    suspend fun obtenerPreguntasSecretas(): PreguntasResponse

    @GET("datos_personales.php")
    suspend fun obtenerDatosPersonales(@Query("idUsuario") idUsuario: Int, @Query("t") t: Long): DatosPersonalesResponse
}

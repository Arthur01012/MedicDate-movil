package com.example.medicdate.Red

import com.example.medicdate.InterfaceApi.ApiService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitCliente {
    private const val BASE_URL = "https://medicdate.innego.net/web/apis/movile/medicdate_api/"

    private class NoCacheInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .build()
            return chain.proceed(request)
        }
    }

    val jsonConfig: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(NoCacheInterceptor())
        .build()

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)
}

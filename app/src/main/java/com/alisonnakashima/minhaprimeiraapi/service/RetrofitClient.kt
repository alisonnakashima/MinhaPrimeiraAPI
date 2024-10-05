package com.alisonnakashima.minhaprimeiraapi.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.0.113:3000/" //esse IP indica a máquina para o emulador

    private val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService = instance.create(ApiService::class.java)

}

package org.example.project.API

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClientClear {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:8080/") // Укажите ваш базовый URL
        .addConverterFactory(ScalarsConverterFactory.create()) // Добавьте конвертер для строк
        .build()
}
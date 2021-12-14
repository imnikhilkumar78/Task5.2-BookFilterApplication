package com.example.bookfilterapplication

import android.app.Application
import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class BookFilterApp: Application(){
    public lateinit var httpApiService:HttpApiService
    override fun onCreate() {
        super.onCreate()
        httpApiService = initHttpApiService()
    }

    private fun initHttpApiService():HttpApiService{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://android-kanini-course.cloud:442/books")
            .addConverterFactory(JacksonConverterFactory.create(ObjectMapper()))
            .build()
        return retrofit.create(HttpApiService::class.java)
    }
}
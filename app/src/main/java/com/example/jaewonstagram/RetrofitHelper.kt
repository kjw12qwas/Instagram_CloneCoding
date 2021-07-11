package com.example.jaewonstagram

import com.example.jaewonstagram.model.api.PictureAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitHelper{

        private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(150, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .build()

    var gson : Gson =  GsonBuilder()
        .setLenient()
        .create()
        private var retrofit = Retrofit.Builder()
            .baseUrl("https://a5ced00334a5.ngrok.io")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        fun getPictureAPI() : PictureAPI {
            return retrofit.create(PictureAPI::class.java)
        }

}
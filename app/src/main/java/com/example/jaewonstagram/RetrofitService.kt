package com.example.jaewonstagram

import android.telecom.Call
import retrofit2.http.*

interface RetrofitService {

    @GET("json/students/")
    fun getStudentsList(): retrofit2.Call<ArrayList<PersonFromServer>>

    @POST("json/students/")
    fun createStudent(
        @Body params : HashMap<String,Any>
    ) : retrofit2.Call<PersonFromServer>

    @POST("json/students/")
    fun createStudentEasy(
        @Body person : PersonFromServer
    ) : retrofit2.Call<PersonFromServer>

    @POST("user/signup/")
    @FormUrlEncoded
    fun register(
        @Field("username")username : String,
        @Field("password1")password1 : String,
        @Field("password2")password2 : String
    ) : retrofit2.Call<User>
}
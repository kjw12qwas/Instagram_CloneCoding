package com.example.jaewonstagram.model.api

import com.example.jaewonstagram.model.domain.ListPictureResponse
import com.example.jaewonstagram.model.domain.UploadPictureResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface PictureAPI {

    //사진을 업로드 하는 API
    @Multipart
    @POST("/picture")
    fun uploadPicture(
        @Header("Authorization") Authorization : String,
        @Part file: MultipartBody.Part
    ) : Call<UploadPictureResponse>

    //사진을 조회하는 API
    @GET("/picture")
    fun listPicture(
        @Header("Authorization") Authorization : String
    ) : Call<List<ListPictureResponse>>

}
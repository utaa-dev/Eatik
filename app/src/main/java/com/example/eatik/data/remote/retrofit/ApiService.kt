package com.example.eatik.data.remote.retrofit

import com.example.eatik.data.remote.response.ResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("menu")
    suspend fun getFoods(): List<ResponseItem>

    @Multipart
    @POST("menu") // Sesuaikan endpointnya
    suspend fun addMenu(
        @Part foto: MultipartBody.Part,
        @Part("nama") nama: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("status") status: RequestBody
    )

    @DELETE("menu/{id}")
    suspend fun deleteMenu(@Path("id") id: Int)
}
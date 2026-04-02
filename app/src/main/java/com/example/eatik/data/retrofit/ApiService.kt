package com.example.eatik.data.retrofit

import com.example.eatik.data.response.ResponseItem
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

    // Ambil semua menu
    @GET("menu")
    fun getAllMenu(): Call<List<ResponseItem>>

    // Upload menu dengan gambar
    @Multipart
    @POST("menu")
    fun postMenuWithImage(
        @Part("nama") nama: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part foto: MultipartBody.Part,  // pastikan nama field ini "foto" sama backend
        @Part("status") status: RequestBody
    ): Call<ResponseItem>

    // Hapus menu
    @DELETE("menu/{id}")
    fun deleteMenu(@Path("id") id: Int): Call<Void>
}
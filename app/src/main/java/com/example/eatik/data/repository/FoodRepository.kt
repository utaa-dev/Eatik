package com.example.eatik.data.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.eatik.data.Result
import com.example.eatik.data.local.dao.FoodDao
import com.example.eatik.data.local.entity.FoodEntity
import com.example.eatik.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FoodRepository(
    private val apiService: ApiService,
    private val foodDao: FoodDao
) {

    // 1. GET FOODS (Sinkronisasi API ke Database Lokal)
    fun getFoods(): LiveData<Result<List<FoodEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFoods()
            val data = response.map {
                FoodEntity(
                    id = it.id ?: 0,
                    nama = it.nama ?: "",
                    harga = it.harga?.toDouble() ?: 0.0, // Sesuai Entity (Double)
                    foto = it.foto ?: "",
                    deskripsi = it.deskripsi ?: "",
                    kategori = it.kategori ?: "",
                    status = it.status ?: ""
                )
            }
            foodDao.deleteMenu() // Menghapus data lama (Cache)
            foodDao.insertFoods(data) // Menyimpan data baru
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal memuat data dari server"))
        }

        // EmitSource agar UI terupdate otomatis dari Database Lokal (Offline-first)
        val localData: LiveData<Result<List<FoodEntity>>> = foodDao.getFoods().map { Result.Success(it) }
        emitSource(localData)
    }

    // 2. ADD MENU (Upload Gambar + Data ke API)
    fun addMenu(
        nama: String,
        harga: Double,
        kategori: String,
        deskripsi: String,
        status: String,
        imageUri: Uri,
        context: Context
    ): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val file = uriToFile(imageUri, context)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("foto", file.name, requestImageFile)

            val namaBody = nama.toRequestBody("text/plain".toMediaTypeOrNull())
            val hargaBody = harga.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val kategoriBody = kategori.toRequestBody("text/plain".toMediaTypeOrNull())
            val deskripsiBody = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
            val statusBody = status.toRequestBody("text/plain".toMediaTypeOrNull())

            // 1. Upload ke API
            apiService.addMenu(imagePart, namaBody, hargaBody, kategoriBody, deskripsiBody, statusBody)

            // 2. Ambil ulang data terbaru dari API
            val response = apiService.getFoods()

            val data = response.map {
                FoodEntity(
                    id = it.id ?: 0,
                    nama = it.nama ?: "",
                    harga = it.harga?.toDouble() ?: 0.0,
                    foto = it.foto ?: "",
                    deskripsi = it.deskripsi ?: "",
                    kategori = it.kategori ?: "",
                    status = it.status ?: ""
                )
            }

            // 3. Update database lokal
            foodDao.deleteMenu()
            foodDao.insertFoods(data)

            emit(Result.Success("Menu berhasil ditambahkan"))

        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal menambah menu"))
        }
    }

    // 3. DELETE MENU
    fun deleteMenu(id: Int): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            apiService.deleteMenu(id)
            foodDao.deleteMenuItem(id)
            emit(Result.Success("Menu berhasil dihapus"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal menghapus menu"))
        }
    }

    // HELPER: Konversi URI Gambar ke File
    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val myFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    companion object {
        @Volatile
        private var instance: FoodRepository? = null
        fun getInstance(apiService: ApiService, foodDao: FoodDao): FoodRepository =
            instance ?: synchronized(this) {
                instance ?: FoodRepository(apiService, foodDao)
            }.also { instance = it }
    }
}
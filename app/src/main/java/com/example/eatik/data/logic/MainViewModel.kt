package com.example.eatik.data.logic

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eatik.data.response.ResponseItem
import com.example.eatik.data.retrofit.ApiConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class MainViewModel : ViewModel() {

    private val api = ApiConfig.getApiService()

    private val _menu = MutableLiveData<List<ResponseItem>>()
    val menu: LiveData<List<ResponseItem>> = _menu

    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> = _uploadStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun loadMenu() {
        _isLoading.value = true

        api.getAllMenu().enqueue(object : Callback<List<ResponseItem>> {
            override fun onResponse(
                call: Call<List<ResponseItem>>,
                response: Response<List<ResponseItem>>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _menu.value = response.body()
                    Log.d("MainViewModel", "loadMenu sukses")
                } else {
                    Log.e("MainViewModel", "loadMenu gagal: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "loadMenu error: ${t.message}")
            }
        })
    }

    fun loadMenuIfNeeded() {
        if (_menu.value.isNullOrEmpty()) {
            loadMenu()
        }
    }

    fun addMenu(
        nama: String,
        harga: Double,
        kategori: String,
        deskripsi: String,
        status: String,
        imageUri: Uri,
        context: Context
    ) {
        _isLoading.value = true

        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            if (inputStream == null) {
                _isLoading.value = false
                _uploadStatus.value = false
                return
            }

            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
            }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart =
                MultipartBody.Part.createFormData("foto", file.name, requestFile)

            val hargaString = harga.toBigDecimal().toPlainString()

            val namaPart = nama.toRequestBody("text/plain".toMediaTypeOrNull())
            val hargaPart = hargaString.toRequestBody("text/plain".toMediaTypeOrNull())
            val kategoriPart = kategori.toRequestBody("text/plain".toMediaTypeOrNull())
            val deskripsiPart = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
            val statusPart = status.toRequestBody("text/plain".toMediaTypeOrNull())

            api.postMenuWithImage(
                namaPart,
                hargaPart,
                deskripsiPart,
                kategoriPart,
                imagePart,
                statusPart
            ).enqueue(object : Callback<ResponseItem> {

                override fun onResponse(
                    call: Call<ResponseItem>,
                    response: Response<ResponseItem>
                ) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _uploadStatus.value = true
                        loadMenu()
                    } else {
                        _uploadStatus.value = false
                    }
                }

                override fun onFailure(call: Call<ResponseItem>, t: Throwable) {
                    _isLoading.value = false
                    _uploadStatus.value = false
                }
            })

        } catch (e: Exception) {
            _isLoading.value = false
            _uploadStatus.value = false
        }
    }

    fun deleteMenu(id: Int) {
        _isLoading.value = true

        api.deleteMenu(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    loadMenu()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}
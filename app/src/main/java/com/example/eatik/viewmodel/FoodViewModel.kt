package com.example.eatik.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatik.data.Result
import com.example.eatik.data.local.entity.FoodEntity
import com.example.eatik.data.repository.FoodRepository
import kotlinx.coroutines.launch

class FoodViewModel(private val foodRepository: FoodRepository) : ViewModel() {


    val foods: LiveData<Result<List<FoodEntity>>> = foodRepository.getFoods()


    fun addMenu(
        nama: String,
        harga: Double,
        kategori: String,
        deskripsi: String,
        status: String,
        imageUri: Uri,
        context: Context
    ) = foodRepository.addMenu(nama, harga, kategori, deskripsi, status, imageUri, context)

    fun deleteMenu(id: Int) = foodRepository.deleteMenu(id)
}


package com.example.eatik.di

import android.content.Context
import com.example.eatik.data.local.appdatabase.AppDatabase
import com.example.eatik.data.remote.retrofit.ApiConfig
import com.example.eatik.data.repository.FoodRepository

object Injection {

    fun provideRepository(context: Context): FoodRepository {
        val apiService = ApiConfig.getApiService()
        val database = AppDatabase.getInstance(context)
        val dao = database.foodDao()
        return FoodRepository.getInstance(apiService, dao)
    }
}
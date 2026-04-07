package com.example.eatik.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.eatik.data.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Query("SELECT * FROM foods")
    fun getFoods(): LiveData<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(data: List<FoodEntity>)

    @Query("DELETE FROM foods")
    suspend fun deleteMenu()

    @Query("DELETE FROM foods WHERE id = :id")
    suspend fun deleteMenuItem(id: Int)

    @Delete
    suspend fun deleteMenu(menu: FoodEntity)
}


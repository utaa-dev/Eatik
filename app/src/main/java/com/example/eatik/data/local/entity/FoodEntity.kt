package com.example.eatik.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey val id: Int,

    @field:ColumnInfo(name = "nama")
    val nama: String,

    @field:ColumnInfo(name = "harga")
    val harga: Double,

    @field:ColumnInfo(name = "foto")
    val foto: String,

    @field:ColumnInfo(name = "deskripsi")
    val deskripsi: String,

    @field:ColumnInfo(name = "kategori"
    ) val kategori: String,

    @field:ColumnInfo(name = "status")
    val status: String

)
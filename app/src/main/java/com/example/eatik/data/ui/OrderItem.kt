package com.example.eatik.data.ui

data class OrderItem(
    val nama: String,
    val jumlah: Int,
    val harga: Double,
    val foto: String, // URL / drawable
    val status: String // "Proses", "Dijalan", "Selesai"

)
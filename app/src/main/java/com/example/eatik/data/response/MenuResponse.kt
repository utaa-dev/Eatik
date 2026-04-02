package com.example.eatik.data.response

import com.google.gson.annotations.SerializedName

data class MenuResponse(

	@field:SerializedName("Response")
	val response: List<ResponseItem>
)

data class ResponseItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("harga")
	val harga: Double,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("kategori")
	val kategori: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("status")
	val status: String
)

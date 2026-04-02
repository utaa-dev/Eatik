package com.example.eatik.data.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.eatik.R
import com.example.eatik.data.logic.MainViewModel
import com.example.eatik.databinding.BottomAddMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddMenuBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomAddMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomAddMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup spinner kategori & status
        val kategoriAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.kategori_array)
        )
        kategoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = kategoriAdapter

        val statusAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.status_array)
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = statusAdapter

        // Pilih gambar
        binding.selectImage.setOnClickListener {
            BottomSelectImage { uri ->
                selectedImageUri = uri
                binding.imgPreview.setImageURI(uri)
            }.show(parentFragmentManager, "SelectImage")
        }

        // Animasi touch
        binding.selectImage.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.startAnimation(AnimationUtils.loadAnimation(v.context, R.anim.scale_in))
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.startAnimation(AnimationUtils.loadAnimation(v.context, R.anim.scale_out))
            }
            false
        }

        // Tombol simpan
        binding.btnSave.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val hargaText = binding.etHarga.text.toString().trim()
            val deskripsi = binding.etDeskripsi.text.toString().trim()
            val kategori = binding.spinnerKategori.selectedItem.toString()
            val status = binding.spinnerStatus.selectedItem.toString()

            if (nama.isEmpty() || hargaText.isEmpty()) {
                Toast.makeText(requireContext(), "Nama dan harga wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedImageUri == null) {
                Toast.makeText(requireContext(), "Pilih foto dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val harga = hargaText.toDoubleOrNull()
            if (harga == null) {
                Toast.makeText(requireContext(), "Harga tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnSave.isEnabled = false
            binding.btnSave.text = "Menyimpan..."

            // observer sekali pakai
            val observer = object : androidx.lifecycle.Observer<Boolean> {
                override fun onChanged(success: Boolean) {
                    if (success) {
                        Toast.makeText(requireContext(), "Menu berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Gagal upload menu", Toast.LENGTH_SHORT).show()
                        binding.btnSave.isEnabled = true
                        binding.btnSave.text = "Tambah Menu"
                    }
                    viewModel.uploadStatus.removeObserver(this)
                }
            }
            viewModel.uploadStatus.observe(viewLifecycleOwner, observer)

            // kirim ke ViewModel
            selectedImageUri?.let { uri ->
                viewModel.addMenu(nama, harga, kategori, deskripsi, status, uri, requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
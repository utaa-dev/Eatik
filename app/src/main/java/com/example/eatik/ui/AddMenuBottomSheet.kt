package com.example.eatik.ui

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
import androidx.lifecycle.observe
import com.example.eatik.R
import com.example.eatik.databinding.BottomAddMenuBinding
import com.example.eatik.viewmodel.FoodViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddMenuBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomAddMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodViewModel by activityViewModels<FoodViewModel>()


    private var selectedImageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomAddMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinners()
        setupImagePicker()
        setupSaveButton()
    }

    private fun setupSpinners() {
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
    }

    private fun setupImagePicker() {
        binding.selectImage.setOnClickListener {
            BottomSelectImage { uri ->
                selectedImageUri = uri
                binding.imgPreview.setImageURI(uri)
                binding.imgPreview.visibility = View.VISIBLE // Pastikan preview muncul
            }.show(parentFragmentManager, "SelectImage")
        }

        binding.selectImage.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.startAnimation(AnimationUtils.loadAnimation(v.context, R.anim.scale_in))
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.startAnimation(AnimationUtils.loadAnimation(v.context, R.anim.scale_out))
            }
            false
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val hargaText = binding.etHarga.text.toString().trim()
            val deskripsi = binding.etDeskripsi.text.toString().trim()
            val kategori = binding.spinnerKategori.selectedItem.toString()
            val status = binding.spinnerStatus.selectedItem.toString()
            val foto = selectedImageUri

            // 1. Validasi Input
            if (nama.isEmpty() || hargaText.isEmpty() || foto == null || deskripsi.isEmpty()) {
                Toast.makeText(requireContext(), "Semua data wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val harga = hargaText.toDoubleOrNull() ?: 0.0

            // 2. Loading State
            binding.btnSave.isEnabled = false
            binding.btnSave.text = "Menyimpan..."

            // 3. Observer untuk memantau hasil upload
            viewModel.addMenu(nama, harga, kategori, deskripsi, status, foto, requireContext())
                .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    Toast.makeText(requireContext(), "Menu berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    dismiss() // Tutup BottomSheet jika berhasil
                } else {
                    Toast.makeText(requireContext(), "Gagal menambahkan menu", Toast.LENGTH_SHORT).show()
                    binding.btnSave.isEnabled = true
                    binding.btnSave.text = "Simpan"
                }
            }

            // 4. Kirim data ke ViewModel
            viewModel.addMenu(
                nama,
                harga,
                kategori,
                deskripsi,
                status,
                foto,
                requireContext()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
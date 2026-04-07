package com.example.eatik.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eatik.adapter.MenuAdapter
import com.example.eatik.data.local.entity.FoodEntity
import com.example.eatik.databinding.FragmentMinumanBinding
import com.example.eatik.viewmodel.FoodViewModel
import com.example.eatik.viewmodel.ViewModelFactory

class MinumanFragment : Fragment() {

    private var _binding: FragmentMinumanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MenuAdapter

    // Gunakan ViewModelFactory agar inisialisasi Repository benar
    private val viewModel: FoodViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMinumanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeMenu()
    }

    private fun setupRecyclerView() {
        adapter = MenuAdapter(
            object : MenuAdapter.OnDeleteClickListener {
                override fun onDeleteClick(menu: FoodEntity) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Hapus Menu")
                        .setMessage("Yakin mau hapus ${menu.nama}?")
                        .setPositiveButton("Hapus") { _, _ ->
                            viewModel.deleteMenu(id)
                        }
                        .setNegativeButton("Batal", null)
                        .show()
                }
            }
        )

        binding.recyclerMinuman.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerMinuman.adapter = adapter
    }

    private fun observeMenu() {
        viewModel.foods.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.eatik.data.Result.Success -> {
                    val data = result.data
                    if (data != null) {

                        val filteredList = data.filter { it.kategori.uppercase() == "MINUMAN" }
                        adapter.submitList(filteredList)
                    }
                }
                is com.example.eatik.data.Result.Loading -> {
                    // Tampilkan loading jika ada ProgressBar
                }
                is com.example.eatik.data.Result.Error -> {
                    // Tampilkan pesan error jika perlu
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
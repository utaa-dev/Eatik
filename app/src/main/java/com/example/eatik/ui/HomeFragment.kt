package com.example.eatik.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eatik.adapter.MenuAdapter
import com.example.eatik.data.Result
import com.example.eatik.data.local.entity.FoodEntity
import com.example.eatik.data.remote.response.ResponseItem
import com.example.eatik.databinding.FragmentHomeBinding
import com.example.eatik.viewmodel.FoodViewModel
import com.example.eatik.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter

    private val viewModel: FoodViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupRecycler()

        val factory = ViewModelFactory.getInstance(requireContext())

        viewModel.foods.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar?.visibility = View.GONE

                    val data = result.data
                    if (data != null) {
                        adapter.submitList(data)
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setupRecycler() {
        adapter = MenuAdapter(object : MenuAdapter.OnDeleteClickListener {

            override fun onDeleteClick(menu: FoodEntity) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Menu")
                    .setMessage("Yakin hapus ${menu.nama}?")
                    .setPositiveButton("Hapus") { _, _ ->
                        viewModel.deleteMenu(menu.id).observe(viewLifecycleOwner) {result ->
                            if (result is Result.Success) {
                                Toast.makeText(requireContext(), "Menu berhasil dihapus", Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        })

        binding.recyclerMenu.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerMenu.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
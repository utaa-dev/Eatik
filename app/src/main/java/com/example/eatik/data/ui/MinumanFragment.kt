package com.example.eatik.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eatik.R
import com.example.eatik.data.logic.MainViewModel
import com.example.eatik.data.response.ResponseItem
import com.example.eatik.data.ui.MenuAdapter
import com.example.eatik.databinding.FragmentMinumanBinding

class MinumanFragment : Fragment() {

    private var _binding: FragmentMinumanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MenuAdapter
    private val viewModel: MainViewModel by activityViewModels()

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
        viewModel.loadMenuIfNeeded()
    }

    private fun setupRecyclerView() {
        adapter = MenuAdapter(
            object : MenuAdapter.OnDeleteClickListener {
                override fun onDeleteClick(menu: ResponseItem) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Hapus Menu")
                        .setMessage("Yakin mau hapus ${menu.nama}?")
                        .setPositiveButton("Hapus") { _, _ ->
                            viewModel.deleteMenu(menu.id)
                        }
                        .setNegativeButton("Batal", null)
                        .show()
                }
            }
            // onItemClick dihapus, klik item tidak membuka detail
        )

        binding.recyclerMinuman.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerMinuman.adapter = adapter
    }

    private fun observeMenu() {
        viewModel.menu.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.filter { it.kategori.uppercase() == "MINUMAN" })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
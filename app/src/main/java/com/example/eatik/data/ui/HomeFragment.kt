package com.example.eatik.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eatik.R
import com.example.eatik.data.logic.MainViewModel
import com.example.eatik.data.response.ResponseItem
import com.example.eatik.data.ui.MenuAdapter
import com.example.eatik.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter
    private val viewModel: MainViewModel by activityViewModels()

    private val bannerDelay = 8000L
    private var currentPage = 0
    private var autoScrollJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupBannerSlider()

        viewModel.menu.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

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
            // onItemClick sengaja dihapus, jadi klik item tidak melakukan apa-apa
        )

        binding.recyclerMenu.layoutManager =
            GridLayoutManager(requireContext(), 2)

        binding.recyclerMenu.adapter = adapter
    }

    private fun setupBannerSlider() {
        val imageList = listOf(
            "https://i.pinimg.com/736x/c6/c6/26/c6c626b7618ca4794d8d711739cdd1f6.jpg",
            "https://i.pinimg.com/1200x/3b/22/79/3b22792450094b5da76781c5049f825e.jpg",
            "https://i.pinimg.com/736x/b5/6b/36/b56b3611ba55876c6c6d3c8b8ed7ba68.jpg"
        )

        val bannerAdapter = BannerAdapter(imageList)
        binding.viewPagerBanner.adapter = bannerAdapter

        TabLayoutMediator(binding.tabIndicator, binding.viewPagerBanner) { _, _ -> }.attach()

        startAutoScroll(imageList.size)
    }

    private fun startAutoScroll(size: Int) {
        if (size == 0) return

        autoScrollJob?.cancel()

        autoScrollJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                delay(bannerDelay)
                currentPage = (currentPage + 1) % size
                binding.viewPagerBanner.setCurrentItem(currentPage, true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll(binding.viewPagerBanner.adapter?.itemCount ?: 0)
    }

    override fun onPause() {
        super.onPause()
        autoScrollJob?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
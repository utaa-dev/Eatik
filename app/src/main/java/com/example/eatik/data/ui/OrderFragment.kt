package com.example.eatik.data.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eatik.R
import com.example.eatik.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var adapter: OrderAdapter
    val dummyList = listOf(
        OrderItem("Mie Goreng", 2, 25000.0, "https://i.pinimg.com/1200x/d8/8e/b3/d88eb35eaa368c8a198c2aee47176709.jpg","Proses"),
        OrderItem("Es Teh", 1, 10000.0, "https://i.pinimg.com/1200x/bf/7b/cf/bf7bcf63ab01a0c0a8e67fb0c912b07c.jpg","Dijalan"),
        OrderItem("Nasi Goreng", 3, 30000.0, "https://i.pinimg.com/736x/11/a0/8f/11a08f16b0beaa1eefdb30583f1da8f6.jpg","Selesai"),
        OrderItem("Kopi Hitam", 2, 15000.0, "https://i.pinimg.com/736x/4a/55/d4/4a55d4b47ce67e3554abef63ddf8e5a5.jpg","Proses"),
        OrderItem("Mie Goreng", 2, 25000.0, "https://i.pinimg.com/1200x/d8/8e/b3/d88eb35eaa368c8a198c2aee47176709.jpg","Proses"),
        OrderItem("Es Teh", 1, 10000.0, "https://i.pinimg.com/1200x/bf/7b/cf/bf7bcf63ab01a0c0a8e67fb0c912b07c.jpg","Dijalan"),
        OrderItem("Nasi Goreng", 3, 30000.0, "https://i.pinimg.com/736x/11/a0/8f/11a08f16b0beaa1eefdb30583f1da8f6.jpg","Selesai"),
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OrderAdapter(dummyList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }
}

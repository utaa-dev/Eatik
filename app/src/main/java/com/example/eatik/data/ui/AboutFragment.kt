package com.example.eatik.data.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.eatik.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        // tombol email developer
        val btnEmail = view.findViewById<Button>(R.id.btnEmailDeveloper)
        btnEmail.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("putraaramadhan.121@gmail.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Pertanyaan tentang Eatik")
                }
                startActivity(Intent.createChooser(intent, "Pilih aplikasi email"))
            } catch (e: Exception) {
                // kalau gak ada email app, kasih toast biar user tahu
                Toast.makeText(requireContext(), "Tidak ada aplikasi email terpasang", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}
package com.example.eatik.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.eatik.R

class SettingsFragment : Fragment() {

    private lateinit var switchDarkMode: Switch
    private lateinit var switchNotif: Switch
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        switchDarkMode = view.findViewById(R.id.switchDarkMode)
        switchNotif = view.findViewById(R.id.switchNotif)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Dark mode switch listener
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // optional: simpan preferensi ke SharedPreferences
        }

        // Notifikasi switch listener
        switchNotif.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Notifikasi diaktifkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Notifikasi dimatikan", Toast.LENGTH_SHORT).show()
            }
            // optional: simpan preferensi notifikasi ke SharedPreferences
        }

        // Logout button listener
        btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            // tambahkan kode logout sebenarnya sesuai implementasi login
        }

        return view
    }
}
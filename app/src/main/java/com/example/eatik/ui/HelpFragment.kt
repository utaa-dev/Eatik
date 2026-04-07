package com.example.eatik.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eatik.R
import android.widget.Button

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_help, container, false)

        // tombol email
        val btnEmail = view.findViewById<Button>(R.id.btn_email_support)
        btnEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@eatik.com")
                putExtra(Intent.EXTRA_SUBJECT, "Bantuan Aplikasi Eatik")
            }
            startActivity(intent)
        }

        // tombol WhatsApp
        val btnWA = view.findViewById<Button>(R.id.btn_whatsapp_support)
        btnWA.setOnClickListener {
            val url = "https://wa.me/6281234567890" // ganti nomor support
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        return view
    }
}
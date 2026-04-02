package com.example.eatik.data.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.eatik.R
import com.example.eatik.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.tvName.text = "Utaa"
            binding.tvEmail.text = "utaa@example.com"
            binding.imgProfile.setImageResource(R.drawable.miku)

            binding.tvSettings.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
            }

            binding.tvHelp.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_helpFragment)
            }

            binding.tvAbout.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
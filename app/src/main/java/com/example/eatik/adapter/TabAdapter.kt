package com.example.eatik.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eatik.ui.MakananFragment
import com.example.eatik.ui.MinumanFragment

class TabAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MakananFragment()
            1 -> MinumanFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getItemCount(): Int = 2
}
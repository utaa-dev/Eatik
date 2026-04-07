package com.example.eatik.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatik.R
import com.example.eatik.data.local.entity.FoodEntity
import com.example.eatik.databinding.ItemMenuBinding

class MenuAdapter(
    private val onDeleteClickListener: OnDeleteClickListener,
    private val onItemClick: ((FoodEntity) -> Unit)? = null
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(menu: FoodEntity)
    }

    private var menuList: List<FoodEntity> = listOf()

    fun submitList(newList: List<FoodEntity>) {
        menuList = newList
        notifyDataSetChanged()
    }

    inner class MenuViewHolder(val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menuList[position]

        holder.binding.tvName.text = menu.nama
        holder.binding.tvPrice.text = "Rp ${menu.harga}"
        holder.binding.tvDesc.text = menu.deskripsi
        holder.binding.tvCategory.text = menu.kategori

        // Logika Status
        if (menu.status.equals("TERSEDIA", ignoreCase = true)) {
            holder.binding.tvStatusBadge.text = "Tersedia"
            holder.binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_status_available)
        } else {
            holder.binding.tvStatusBadge.text = "Tidak Tersedia"
            holder.binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_status_unavailable)
        }

        // Gambar
        Glide.with(holder.itemView.context)
            .load("http://192.168.0.238:8080/uploads/${menu.foto}")
            .placeholder(R.drawable.ic_launcher_background) // Tambahkan placeholder jika ada
            .into(holder.binding.ivFood)

        // Animasi
        setAnimation(holder.itemView)

        holder.itemView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> view.startAnimation(
                    AnimationUtils.loadAnimation(view.context, R.anim.scale_in)
                )
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> view.startAnimation(
                    AnimationUtils.loadAnimation(view.context, R.anim.scale_out)
                )
            }
            false
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(menu)
        }

        holder.binding.btnMinus.setOnClickListener {
            onDeleteClickListener.onDeleteClick(menu)
        }
    }

    private fun setAnimation(view: View) {
        view.startAnimation(
            AnimationUtils.loadAnimation(view.context, R.anim.item_animation)
        )
    }
}
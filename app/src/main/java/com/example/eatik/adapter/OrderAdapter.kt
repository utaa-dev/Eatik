package com.example.eatik.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatik.R
import com.example.eatik.ui.OrderItem

class OrderAdapter(private var orderList: List<OrderItem>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = orderList[position]
        holder.tvNama.text = item.nama
        holder.tvJumlah.text = "Qty: ${item.jumlah}"
        holder.tvHarga.text = "Rp ${item.harga}"

        Glide.with(holder.itemView.context)
            .load(item.foto) // bisa URL atau drawable
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.itemView.findViewById(R.id.ivFood))

        // set status
        holder.tvStatus.text = item.status
        when (item.status) {
            "Proses" -> holder.tvStatus.setBackgroundResource(R.drawable.bg_status_proses)
            "Dijalan" -> holder.tvStatus.setBackgroundResource(R.drawable.bg_status_dijalan)
            "Selesai" -> holder.tvStatus.setBackgroundResource(R.drawable.bg_status_selesai)
        }

        setAnimation(holder.itemView)

        holder.itemView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.startAnimation(
                        AnimationUtils.loadAnimation(view.context, R.anim.scale_in)
                    )
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    view.startAnimation(
                        AnimationUtils.loadAnimation(view.context, R.anim.scale_out)
                    )
                }
            }
            false
        }
    }
    private fun setAnimation(view: View) {
        view.setAnimation(
            AnimationUtils.loadAnimation(
                view.context,
                R.anim.item_animation
            )
        )
    }
}